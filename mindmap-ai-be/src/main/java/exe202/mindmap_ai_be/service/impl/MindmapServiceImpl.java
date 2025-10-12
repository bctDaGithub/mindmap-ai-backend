package exe202.mindmap_ai_be.service.impl;

import exe202.mindmap_ai_be.dto.request.CreateMindmapRequest;
import exe202.mindmap_ai_be.dto.request.UpdateMindmapRequest;
import exe202.mindmap_ai_be.dto.response.MindmapResponse;
import exe202.mindmap_ai_be.entity.Edge;
import exe202.mindmap_ai_be.entity.Mindmap;
import exe202.mindmap_ai_be.entity.Node;
import exe202.mindmap_ai_be.entity.enums.Shape;
import exe202.mindmap_ai_be.repository.EdgeRepository;
import exe202.mindmap_ai_be.repository.MindmapRepository;
import exe202.mindmap_ai_be.repository.NodeRepository;
import exe202.mindmap_ai_be.service.MindmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MindmapServiceImpl implements MindmapService {

    private final MindmapRepository mindmapRepository;
    private final NodeRepository nodeRepository;
    private final EdgeRepository edgeRepository;

    // ============= Mindmap CRUD Implementation =============

    @Override
    public Mono<MindmapResponse> createMindmap(CreateMindmapRequest request, Long ownerId) {
        Mindmap mindmap = new Mindmap();
        mindmap.setTitle(request.getTitle());
        mindmap.setDescription(request.getDescription());
        mindmap.setWorkspaceId(request.getWorkspaceId());
        mindmap.setOwnerId(ownerId);
        mindmap.setPublic(request.isPublic());

        Timestamp now = Timestamp.from(Instant.now());
        mindmap.setCreatedAt(now);
        mindmap.setUpdatedAt(now);

        return mindmapRepository.save(mindmap)
                .map(this::toMindmapResponse);
    }

    @Override
    public Mono<MindmapResponse> getMindmapById(Long mindmapId) {
        return mindmapRepository.findById(mindmapId)
                .map(this::toMindmapResponse);
    }

    @Override
    public Mono<Mindmap> getMindmapEntityById(Long mindmapId) {
        return mindmapRepository.findById(mindmapId);
    }

    @Override
    public Mono<MindmapResponse> updateMindmap(Long mindmapId, UpdateMindmapRequest request, Long userId) {
        return mindmapRepository.findById(mindmapId)
                .flatMap(mindmap -> {
                    // Check ownership (optional - you can add proper authorization later)
                    if (request.getTitle() != null) {
                        mindmap.setTitle(request.getTitle());
                    }
                    if (request.getDescription() != null) {
                        mindmap.setDescription(request.getDescription());
                    }
                    if (request.getWorkspaceId() != null) {
                        mindmap.setWorkspaceId(request.getWorkspaceId());
                    }
                    if (request.getIsPublic() != null) {
                        mindmap.setPublic(request.getIsPublic());
                    }

                    mindmap.setUpdatedAt(Timestamp.from(Instant.now()));

                    return mindmapRepository.save(mindmap);
                })
                .map(this::toMindmapResponse);
    }

    @Override
    public Mono<Void> deleteMindmap(Long mindmapId, Long userId) {
        return mindmapRepository.findById(mindmapId)
                .flatMap(mindmap -> {
                    // Check ownership (optional - you can add proper authorization later)
                    return mindmapRepository.deleteById(mindmapId);
                });
    }

    @Override
    public Flux<MindmapResponse> getAllMindmaps() {
        return mindmapRepository.findAll()
                .map(this::toMindmapResponse);
    }

    @Override
    public Flux<MindmapResponse> getMindmapsByOwnerId(Long ownerId) {
        return mindmapRepository.findAllByOwnerId(ownerId)
                .map(this::toMindmapResponse);
    }

    @Override
    public Flux<MindmapResponse> getMindmapsByWorkspaceId(Long workspaceId) {
        return mindmapRepository.findAllByWorkspaceId(workspaceId)
                .map(this::toMindmapResponse);
    }

    @Override
    public Flux<MindmapResponse> getPublicMindmaps() {
        return mindmapRepository.findAllByIsPublic(true)
                .map(this::toMindmapResponse);
    }

    // ============= Node Operations Implementation =============

    @Override
    public Mono<Node> createNode(Node node) {
        if (node.getCreateAt() == null) {
            node.setCreateAt(Instant.now());
        }
        return nodeRepository.save(node);
    }

    @Override
    public Mono<Node> updateNode(Node node) {
        return nodeRepository.save(node);
    }

    @Override
    public Mono<Node> getNodeById(Long nodeId) {
        return nodeRepository.findById(nodeId);
    }

    @Override
    public Mono<Void> deleteNode(Long nodeId) {
        return nodeRepository.deleteById(nodeId);
    }

    @Override
    public Flux<Node> listNodes(Long mindmapId) {
        return nodeRepository.findAllByMindMapId(mindmapId);
    }

    @Override
    public Mono<Node> moveNode(Long nodeId, double positionX, double positionY) {
        return nodeRepository.findById(nodeId)
                .flatMap(node -> {
                    node.setPositionX(positionX);
                    node.setPositionY(positionY);
                    return nodeRepository.save(node);
                });
    }

    @Override
    public Mono<Node> updateNodeColor(Long nodeId, String color) {
        return nodeRepository.findById(nodeId)
                .flatMap(node -> {
                    node.setColor(color);
                    return nodeRepository.save(node);
                });
    }

    @Override
    public Mono<Node> updateNodeShape(Long nodeId, String shape) {
        return nodeRepository.findById(nodeId)
                .flatMap(node -> {
                    try {
                        node.setShape(Shape.valueOf(shape.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        // Keep existing shape if invalid
                    }
                    return nodeRepository.save(node);
                });
    }

    @Override
    public Mono<Node> updateNodeContent(Long nodeId, String content) {
        return nodeRepository.findById(nodeId)
                .flatMap(node -> {
                    node.setContent(content);
                    return nodeRepository.save(node);
                });
    }

    // ============= Edge Operations Implementation =============

    @Override
    public Mono<Edge> createEdge(Edge edge) {
        return edgeRepository.save(edge);
    }

    @Override
    public Mono<Edge> updateEdge(Edge edge) {
        return edgeRepository.save(edge);
    }

    @Override
    public Mono<Edge> getEdgeById(Long edgeId) {
        return edgeRepository.findById(edgeId);
    }

    @Override
    public Mono<Void> deleteEdge(Long edgeId) {
        return edgeRepository.deleteById(edgeId);
    }

    @Override
    public Flux<Edge> listEdges(Long mindmapId) {
        return edgeRepository.findAllByMindmapId(mindmapId);
    }

    @Override
    public Mono<Edge> updateEdgeLabel(Long edgeId, String label) {
        return edgeRepository.findById(edgeId)
                .flatMap(edge -> {
                    edge.setLabel(label);
                    return edgeRepository.save(edge);
                });
    }

    // ============= Helper Methods =============

    private MindmapResponse toMindmapResponse(Mindmap mindmap) {
        return MindmapResponse.builder()
                .mindMapId(mindmap.getMindMapId())
                .title(mindmap.getTitle())
                .description(mindmap.getDescription())
                .workspaceId(mindmap.getWorkspaceId())
                .ownerId(mindmap.getOwnerId())
                .isPublic(mindmap.isPublic())
                .createdAt(mindmap.getCreatedAt())
                .updatedAt(mindmap.getUpdatedAt())
                .build();
    }
}
