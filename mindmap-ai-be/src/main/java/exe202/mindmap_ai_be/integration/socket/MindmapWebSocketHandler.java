package exe202.mindmap_ai_be.integration.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exe202.mindmap_ai_be.dto.request.MindmapEvent;
import exe202.mindmap_ai_be.entity.Edge;
import exe202.mindmap_ai_be.entity.Node;
import exe202.mindmap_ai_be.entity.enums.EventType;
import exe202.mindmap_ai_be.service.MindmapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MindmapWebSocketHandler implements WebSocketHandler {

    private final ObjectMapper mapper;
    private final MindmapService service;

    // Một sink per mindmapId để broadcast đến tất cả clients trong cùng room
    private final Map<Long, Sinks.Many<String>> roomSinks = new ConcurrentHashMap<>();

    // Track active sessions per mindmap
    private final Map<Long, Integer> activeUsers = new ConcurrentHashMap<>();

    public MindmapWebSocketHandler(@Qualifier("mindmapServiceImpl") MindmapService service) {
        this.service = service;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Long mindmapId = getMindmapIdFrom(session);
        Long userId = getUserIdFrom(session);

        if (mindmapId == null || mindmapId == 0L) {
            return session.close();
        }

        // Get or create sink for this mindmap room
        Sinks.Many<String> sink = roomSinks.computeIfAbsent(mindmapId,
                id -> Sinks.many().multicast().onBackpressureBuffer());

        // Track user join
        activeUsers.merge(mindmapId, 1, Integer::sum);

        // Send initial snapshot to new client
        Mono<Void> initialSync = sendInitialSnapshot(mindmapId, sink);

        // Broadcast user join event
        broadcastEvent(sink, new MindmapEvent(
                EventType.USER_JOIN,
                mindmapId,
                userId,
                "user",
                Map.of("userId", userId, "activeUsers", activeUsers.get(mindmapId)),
                Instant.now()
        ));

        // Handle incoming messages from client
        Flux<String> input = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(text -> handleIncomingEvent(mindmapId, userId, text, sink))
                .doFinally(signalType -> {
                    // Handle user disconnect
                    activeUsers.computeIfPresent(mindmapId, (k, v) -> v > 1 ? v - 1 : null);
                    broadcastEvent(sink, new MindmapEvent(
                            EventType.USER_LEAVE,
                            mindmapId,
                            userId,
                            "user",
                            Map.of("userId", userId, "activeUsers", activeUsers.getOrDefault(mindmapId, 0)),
                            Instant.now()
                    ));

                    // Clean up sink if no more users
                    if (activeUsers.get(mindmapId) == null) {
                        roomSinks.remove(mindmapId);
                    }
                });

        // Send broadcast messages to client
        Flux<WebSocketMessage> output = sink.asFlux()
                .map(session::textMessage);

        return initialSync.then(session.send(output).and(input.then()));
    }

    private Mono<Void> sendInitialSnapshot(Long mindmapId, Sinks.Many<String> sink) {
        return Mono.zip(
                service.listNodes(mindmapId).collectList(),
                service.listEdges(mindmapId).collectList()
        ).flatMap(tuple -> {
            Map<String, Object> payload = Map.of(
                    "nodes", tuple.getT1(),
                    "edges", tuple.getT2()
            );
            MindmapEvent snapshot = new MindmapEvent(
                    EventType.SYNC_SNAPSHOT,
                    mindmapId,
                    null,
                    "snapshot",
                    payload,
                    Instant.now()
            );
            broadcastEvent(sink, snapshot);
            return Mono.empty();
        }).onErrorResume(e -> {
            log.error("Error sending initial snapshot for mindmap {}: {}", mindmapId, e.getMessage());
            return Mono.empty();
        }).then();
    }

    private Mono<String> handleIncomingEvent(Long mindmapId, Long userId, String text, Sinks.Many<String> sink) {
        try {
            MindmapEvent event = mapper.readValue(text, MindmapEvent.class);
            event.setMindMapId(mindmapId);
            event.setUserId(userId);
            event.setTimestamp(Instant.now());

            return processEvent(event, sink)
                    .then(Mono.just(text))
                    .onErrorResume(e -> {
                        log.error("Error processing event: {}", e.getMessage());
                        broadcastError(sink, mindmapId, userId, e.getMessage());
                        return Mono.empty();
                    });
        } catch (Exception e) {
            log.error("Invalid message format: {}", e.getMessage());
            broadcastError(sink, mindmapId, userId, "Invalid message format");
            return Mono.empty();
        }
    }

    private Mono<Void> processEvent(MindmapEvent event, Sinks.Many<String> sink) {
        return switch (event.getEventType()) {
            case ADD_NODE -> handleAddNode(event, sink);
            case UPDATE_NODE -> handleUpdateNode(event, sink);
            case DELETE_NODE -> handleDeleteNode(event, sink);
            case MOVE_NODE -> handleMoveNode(event, sink);
            case UPDATE_NODE_COLOR -> handleUpdateNodeColor(event, sink);
            case UPDATE_NODE_SHAPE -> handleUpdateNodeShape(event, sink);
            case UPDATE_NODE_CONTENT -> handleUpdateNodeContent(event, sink);

            case ADD_EDGE -> handleAddEdge(event, sink);
            case UPDATE_EDGE -> handleUpdateEdge(event, sink);
            case UPDATE_EDGE_LABEL -> handleUpdateEdgeLabel(event, sink);
            case DELETE_EDGE -> handleDeleteEdge(event, sink);

            case USER_CURSOR_MOVE -> handleCursorMove(event, sink);
            case SYNC_REQUEST -> handleSyncRequest(event, sink);

            default -> {
                log.warn("Unknown event type: {}", event.getEventType());
                yield Mono.empty();
            }
        };
    }

    // ============= Node Event Handlers =============

    private Mono<Void> handleAddNode(MindmapEvent event, Sinks.Many<String> sink) {
        Node node = mapper.convertValue(event.getPayload(), Node.class);
        node.setMindMapId(event.getMindMapId());

        return service.createNode(node)
                .doOnNext(saved -> {
                    event.setPayload(Map.of("node", saved));
                    broadcastEvent(sink, event);
                })
                .then();
    }

    private Mono<Void> handleUpdateNode(MindmapEvent event, Sinks.Many<String> sink) {
        Node node = mapper.convertValue(event.getPayload(), Node.class);

        return service.updateNode(node)
                .doOnNext(updated -> {
                    event.setPayload(Map.of("node", updated));
                    broadcastEvent(sink, event);
                })
                .then();
    }

    private Mono<Void> handleDeleteNode(MindmapEvent event, Sinks.Many<String> sink) {
        Long nodeId = getLongFromPayload(event.getPayload(), "nodeId");

        return service.deleteNode(nodeId)
                .doOnSuccess(v -> broadcastEvent(sink, event))
                .then();
    }

    private Mono<Void> handleMoveNode(MindmapEvent event, Sinks.Many<String> sink) {
        Long nodeId = getLongFromPayload(event.getPayload(), "nodeId");
        Double positionX = getDoubleFromPayload(event.getPayload(), "positionX");
        Double positionY = getDoubleFromPayload(event.getPayload(), "positionY");

        if (nodeId == null || positionX == null || positionY == null) {
            return Mono.error(new IllegalArgumentException("Missing required parameters"));
        }

        return service.moveNode(nodeId, positionX, positionY)
                .doOnNext(updated -> {
                    event.setPayload(Map.of("node", updated));
                    broadcastEvent(sink, event);
                })
                .then();
    }

    private Mono<Void> handleUpdateNodeColor(MindmapEvent event, Sinks.Many<String> sink) {
        Long nodeId = getLongFromPayload(event.getPayload(), "nodeId");
        String color = (String) event.getPayload().get("color");

        return service.updateNodeColor(nodeId, color)
                .doOnNext(updated -> {
                    event.setPayload(Map.of("node", updated));
                    broadcastEvent(sink, event);
                })
                .then();
    }

    private Mono<Void> handleUpdateNodeShape(MindmapEvent event, Sinks.Many<String> sink) {
        Long nodeId = getLongFromPayload(event.getPayload(), "nodeId");
        String shape = (String) event.getPayload().get("shape");

        return service.updateNodeShape(nodeId, shape)
                .doOnNext(updated -> {
                    event.setPayload(Map.of("node", updated));
                    broadcastEvent(sink, event);
                })
                .then();
    }

    private Mono<Void> handleUpdateNodeContent(MindmapEvent event, Sinks.Many<String> sink) {
        Long nodeId = getLongFromPayload(event.getPayload(), "nodeId");
        String content = (String) event.getPayload().get("content");

        return service.updateNodeContent(nodeId, content)
                .doOnNext(updated -> {
                    event.setPayload(Map.of("node", updated));
                    broadcastEvent(sink, event);
                })
                .then();
    }

    // ============= Edge Event Handlers =============

    private Mono<Void> handleAddEdge(MindmapEvent event, Sinks.Many<String> sink) {
        Edge edge = mapper.convertValue(event.getPayload(), Edge.class);
        edge.setMindmapId(event.getMindMapId());

        return service.createEdge(edge)
                .doOnNext(saved -> {
                    event.setPayload(Map.of("edge", saved));
                    broadcastEvent(sink, event);
                })
                .then();
    }

    private Mono<Void> handleUpdateEdge(MindmapEvent event, Sinks.Many<String> sink) {
        Edge edge = mapper.convertValue(event.getPayload(), Edge.class);

        return service.updateEdge(edge)
                .doOnNext(updated -> {
                    event.setPayload(Map.of("edge", updated));
                    broadcastEvent(sink, event);
                })
                .then();
    }

    private Mono<Void> handleUpdateEdgeLabel(MindmapEvent event, Sinks.Many<String> sink) {
        Long edgeId = getLongFromPayload(event.getPayload(), "edgeId");
        String label = (String) event.getPayload().get("label");

        return service.updateEdgeLabel(edgeId, label)
                .doOnNext(updated -> {
                    event.setPayload(Map.of("edge", updated));
                    broadcastEvent(sink, event);
                })
                .then();
    }

    private Mono<Void> handleDeleteEdge(MindmapEvent event, Sinks.Many<String> sink) {
        Long edgeId = getLongFromPayload(event.getPayload(), "edgeId");

        return service.deleteEdge(edgeId)
                .doOnSuccess(v -> broadcastEvent(sink, event))
                .then();
    }

    // ============= Other Event Handlers =============

    private Mono<Void> handleCursorMove(MindmapEvent event, Sinks.Many<String> sink) {
        // Just broadcast cursor position without persisting
        broadcastEvent(sink, event);
        return Mono.empty();
    }

    private Mono<Void> handleSyncRequest(MindmapEvent event, Sinks.Many<String> sink) {
        return sendInitialSnapshot(event.getMindMapId(), sink);
    }

    // ============= Helper Methods =============

    private void broadcastEvent(Sinks.Many<String> sink, MindmapEvent event) {
        try {
            String json = mapper.writeValueAsString(event);
            sink.tryEmitNext(json);
        } catch (JsonProcessingException e) {
            log.error("Error serializing event: {}", e.getMessage());
        }
    }

    private void broadcastError(Sinks.Many<String> sink, Long mindmapId, Long userId, String errorMessage) {
        MindmapEvent errorEvent = new MindmapEvent(
                EventType.ERROR,
                mindmapId,
                userId,
                "error",
                Map.of("message", errorMessage),
                Instant.now()
        );
        broadcastEvent(sink, errorEvent);
    }

    private Long getMindmapIdFrom(WebSocketSession session) {
        String query = session.getHandshakeInfo().getUri().getQuery();
        return extractLongParam(query, "mindmapId");
    }

    private Long getUserIdFrom(WebSocketSession session) {
        String query = session.getHandshakeInfo().getUri().getQuery();
        return extractLongParam(query, "userId");
    }

    private Long extractLongParam(String query, String paramName) {
        if (query != null && query.contains(paramName + "=")) {
            try {
                String value = query.split(paramName + "=")[1].split("&")[0];
                return Long.parseLong(value);
            } catch (Exception e) {
                log.warn("Failed to extract {} from query: {}", paramName, query);
            }
        }
        return null;
    }

    private Long getLongFromPayload(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }

    private Double getDoubleFromPayload(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return null;
    }
}
