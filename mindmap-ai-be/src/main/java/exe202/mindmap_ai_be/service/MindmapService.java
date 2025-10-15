package exe202.mindmap_ai_be.service;

import exe202.mindmap_ai_be.dto.request.CreateMindmapRequest;
import exe202.mindmap_ai_be.dto.request.UpdateMindmapRequest;
import exe202.mindmap_ai_be.dto.response.MindmapResponse;
import exe202.mindmap_ai_be.entity.Edge;
import exe202.mindmap_ai_be.entity.Mindmap;
import exe202.mindmap_ai_be.entity.Node;
import exe202.mindmap_ai_be.entity.UserMindmapPermission;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MindmapService {
    // ============= Mindmap CRUD =============
    Mono<MindmapResponse> createMindmap(CreateMindmapRequest request, Long ownerId);
    Mono<MindmapResponse> getMindmapById(Long mindmapId);
    Mono<Mindmap> getMindmapEntityById(Long mindmapId);
    Mono<MindmapResponse> updateMindmap(Long mindmapId, UpdateMindmapRequest request, Long userId);
    Mono<Void> deleteMindmap(Long mindmapId, Long userId);
    Flux<MindmapResponse> getAllMindmaps();
    Flux<MindmapResponse> getMindmapsByOwnerId(Long ownerId);
    Flux<MindmapResponse> getMindmapsByWorkspaceId(Long workspaceId);
    Flux<MindmapResponse> getPublicMindmaps();

    // ============= Node operations =============
    Mono<Node> createNode(Node node);
    Mono<Node> updateNode(Node node);
    Mono<Node> getNodeById(Long nodeId);
    Mono<Void> deleteNode(Long nodeId);
    Flux<Node> listNodes(Long mindmapId);

    // Node specific updates
    Mono<Node> moveNode(Long nodeId, double positionX, double positionY);
    Mono<Node> updateNodeColor(Long nodeId, String color);
    Mono<Node> updateNodeShape(Long nodeId, String shape);
    Mono<Node> updateNodeContent(Long nodeId, String content);

    // ============= Edge operations =============
    Mono<Edge> createEdge(Edge edge);
    Mono<Edge> updateEdge(Edge edge);
    Mono<Edge> getEdgeById(Long edgeId);
    Mono<Void> deleteEdge(Long edgeId);
    Flux<Edge> listEdges(Long mindmapId);

    // Edge specific updates
    Mono<Edge> updateEdgeLabel(Long edgeId, String label);

    // ============= Collaboration operations =============
    Mono<Void> inviteUserToMindmap(Long mindmapId, Long targetUserId, String permission, Long ownerId);
    Flux<UserMindmapPermission> getMindmapMembers(Long mindmapId);
    Mono<Void> updateMindmapMemberPermission(Long mindmapId, Long userId, String permission, Long ownerId);
    Mono<Void> removeMindmapMember(Long mindmapId, Long userId, Long ownerId);
}
