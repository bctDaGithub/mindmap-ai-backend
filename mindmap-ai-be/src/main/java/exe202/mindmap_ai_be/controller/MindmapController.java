package exe202.mindmap_ai_be.controller;

import exe202.mindmap_ai_be.dto.request.CreateMindmapRequest;
import exe202.mindmap_ai_be.dto.request.UpdateMindmapRequest;
import exe202.mindmap_ai_be.dto.response.MindmapResponse;
import exe202.mindmap_ai_be.entity.Edge;
import exe202.mindmap_ai_be.entity.Node;
import exe202.mindmap_ai_be.entity.UserMindmapPermission;
import exe202.mindmap_ai_be.service.MindmapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/mindmap")
@RequiredArgsConstructor
@Tag(name = "Mindmap Real-time Collaboration", description = "APIs for real-time mindmap collaboration with WebSocket support")
public class MindmapController {

    private final MindmapService mindmapService;

    // ============= Mindmap CRUD Endpoints =============

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new mindmap", description = "Creates a new mindmap with title and description")
    public Mono<MindmapResponse> createMindmap(
            @Valid @RequestBody CreateMindmapRequest request,
            @Parameter(description = "Owner user ID") @RequestParam(required = false, defaultValue = "1") Long ownerId) {
        return mindmapService.createMindmap(request, ownerId);
    }

    @GetMapping
    @Operation(summary = "Get all mindmaps", description = "Retrieves all mindmaps in the system")
    public Flux<MindmapResponse> getAllMindmaps() {
        return mindmapService.getAllMindmaps();
    }

    @GetMapping("/{mindmapId}")
    @Operation(summary = "Get mindmap by ID", description = "Retrieves a specific mindmap by its ID")
    public Mono<MindmapResponse> getMindmapById(
            @Parameter(description = "Mindmap ID") @PathVariable Long mindmapId) {
        return mindmapService.getMindmapById(mindmapId);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get mindmaps by owner", description = "Retrieves all mindmaps owned by a specific user")
    public Flux<MindmapResponse> getMindmapsByOwnerId(
            @Parameter(description = "Owner user ID") @PathVariable Long ownerId) {
        return mindmapService.getMindmapsByOwnerId(ownerId);
    }

    @GetMapping("/workspace/{workspaceId}")
    @Operation(summary = "Get mindmaps by workspace", description = "Retrieves all mindmaps in a specific workspace")
    public Flux<MindmapResponse> getMindmapsByWorkspaceId(
            @Parameter(description = "Workspace ID") @PathVariable Long workspaceId) {
        return mindmapService.getMindmapsByWorkspaceId(workspaceId);
    }

    @GetMapping("/public")
    @Operation(summary = "Get public mindmaps", description = "Retrieves all public mindmaps")
    public Flux<MindmapResponse> getPublicMindmaps() {
        return mindmapService.getPublicMindmaps();
    }

    @PutMapping("/{mindmapId}")
    @Operation(summary = "Update mindmap", description = "Updates an existing mindmap")
    public Mono<MindmapResponse> updateMindmap(
            @Parameter(description = "Mindmap ID") @PathVariable Long mindmapId,
            @Valid @RequestBody UpdateMindmapRequest request,
            @Parameter(description = "User ID") @RequestParam(required = false, defaultValue = "1") Long userId) {
        return mindmapService.updateMindmap(mindmapId, request, userId);
    }

    @DeleteMapping("/{mindmapId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete mindmap", description = "Deletes a mindmap and all its nodes and edges")
    public Mono<Void> deleteMindmap(
            @Parameter(description = "Mindmap ID") @PathVariable Long mindmapId,
            @Parameter(description = "User ID") @RequestParam(required = false, defaultValue = "1") Long userId) {
        return mindmapService.deleteMindmap(mindmapId, userId);
    }

    // ============= Node Endpoints =============

    @PostMapping("/{mindmapId}/nodes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new node", description = "Creates a new node in the mindmap")
    public Mono<Node> createNode(
            @Parameter(description = "Mindmap ID") @PathVariable Long mindmapId,
            @RequestBody Node node) {
        node.setMindMapId(mindmapId);
        return mindmapService.createNode(node);
    }

    @GetMapping("/{mindmapId}/nodes")
    @Operation(summary = "Get all nodes", description = "Retrieves all nodes for a specific mindmap")
    public Flux<Node> getAllNodes(
            @Parameter(description = "Mindmap ID") @PathVariable Long mindmapId) {
        return mindmapService.listNodes(mindmapId);
    }

    @GetMapping("/nodes/{nodeId}")
    @Operation(summary = "Get node by ID", description = "Retrieves a specific node by its ID")
    public Mono<Node> getNodeById(
            @Parameter(description = "Node ID") @PathVariable Long nodeId) {
        return mindmapService.getNodeById(nodeId);
    }

    @PutMapping("/nodes/{nodeId}")
    @Operation(summary = "Update node", description = "Updates an existing node")
    public Mono<Node> updateNode(
            @Parameter(description = "Node ID") @PathVariable Long nodeId,
            @RequestBody Node node) {
        node.setNodeId(nodeId);
        return mindmapService.updateNode(node);
    }

    @PatchMapping("/nodes/{nodeId}/move")
    @Operation(summary = "Move node", description = "Updates the position of a node")
    public Mono<Node> moveNode(
            @Parameter(description = "Node ID") @PathVariable Long nodeId,
            @Parameter(description = "X coordinate") @RequestParam double positionX,
            @Parameter(description = "Y coordinate") @RequestParam double positionY) {
        return mindmapService.moveNode(nodeId, positionX, positionY);
    }

    @PatchMapping("/nodes/{nodeId}/color")
    @Operation(summary = "Update node color", description = "Updates the color of a node")
    public Mono<Node> updateNodeColor(
            @Parameter(description = "Node ID") @PathVariable Long nodeId,
            @Parameter(description = "Color value (hex or name)") @RequestParam String color) {
        return mindmapService.updateNodeColor(nodeId, color);
    }

    @PatchMapping("/nodes/{nodeId}/shape")
    @Operation(summary = "Update node shape", description = "Updates the shape of a node")
    public Mono<Node> updateNodeShape(
            @Parameter(description = "Node ID") @PathVariable Long nodeId,
            @Parameter(description = "Shape type") @RequestParam String shape) {
        return mindmapService.updateNodeShape(nodeId, shape);
    }

    @PatchMapping("/nodes/{nodeId}/content")
    @Operation(summary = "Update node content", description = "Updates the content/text of a node")
    public Mono<Node> updateNodeContent(
            @Parameter(description = "Node ID") @PathVariable Long nodeId,
            @Parameter(description = "Node content") @RequestParam String content) {
        return mindmapService.updateNodeContent(nodeId, content);
    }

    @DeleteMapping("/nodes/{nodeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete node", description = "Deletes a node from the mindmap")
    public Mono<Void> deleteNode(
            @Parameter(description = "Node ID") @PathVariable Long nodeId) {
        return mindmapService.deleteNode(nodeId);
    }

    // ============= Edge Endpoints =============

    @PostMapping("/{mindmapId}/edges")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new edge", description = "Creates a new edge connecting two nodes")
    public Mono<Edge> createEdge(
            @Parameter(description = "Mindmap ID") @PathVariable Long mindmapId,
            @RequestBody Edge edge) {
        edge.setMindmapId(mindmapId);
        return mindmapService.createEdge(edge);
    }

    @GetMapping("/{mindmapId}/edges")
    @Operation(summary = "Get all edges", description = "Retrieves all edges for a specific mindmap")
    public Flux<Edge> getAllEdges(
            @Parameter(description = "Mindmap ID") @PathVariable Long mindmapId) {
        return mindmapService.listEdges(mindmapId);
    }

    @GetMapping("/edges/{edgeId}")
    @Operation(summary = "Get edge by ID", description = "Retrieves a specific edge by its ID")
    public Mono<Edge> getEdgeById(
            @Parameter(description = "Edge ID") @PathVariable Long edgeId) {
        return mindmapService.getEdgeById(edgeId);
    }

    @PutMapping("/edges/{edgeId}")
    @Operation(summary = "Update edge", description = "Updates an existing edge")
    public Mono<Edge> updateEdge(
            @Parameter(description = "Edge ID") @PathVariable Long edgeId,
            @RequestBody Edge edge) {
        edge.setId(edgeId);
        return mindmapService.updateEdge(edge);
    }

    @PatchMapping("/edges/{edgeId}/label")
    @Operation(summary = "Update edge label", description = "Updates the label of an edge")
    public Mono<Edge> updateEdgeLabel(
            @Parameter(description = "Edge ID") @PathVariable Long edgeId,
            @Parameter(description = "Edge label") @RequestParam String label) {
        return mindmapService.updateEdgeLabel(edgeId, label);
    }

    @DeleteMapping("/edges/{edgeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete edge", description = "Deletes an edge from the mindmap")
    public Mono<Void> deleteEdge(
            @Parameter(description = "Edge ID") @PathVariable Long edgeId) {
        return mindmapService.deleteEdge(edgeId);
    }

    // ============= Collaboration Endpoints =============

    @PostMapping("/{mindmapId}/invite")
    @Operation(summary = "Invite user to mindmap", description = "Owner invites a user to collaborate on a mindmap")
    public Mono<Void> inviteUserToMindmap(
            @PathVariable Long mindmapId,
            @RequestParam Long targetUserId,
            @RequestParam String permission,
            @RequestParam Long ownerId) {
        return mindmapService.inviteUserToMindmap(mindmapId, targetUserId, permission, ownerId);
    }

    @GetMapping("/{mindmapId}/members")
    @Operation(summary = "Get mindmap members", description = "Get all members collaborating on a mindmap")
    public Flux<UserMindmapPermission> getMindmapMembers(@PathVariable Long mindmapId) {
        return mindmapService.getMindmapMembers(mindmapId);
    }

    @PutMapping("/{mindmapId}/members/{userId}/permission")
    @Operation(summary = "Update mindmap member permission", description = "Owner updates a member's permission")
    public Mono<Void> updateMindmapMemberPermission(
            @PathVariable Long mindmapId,
            @PathVariable Long userId,
            @RequestParam String permission,
            @RequestParam Long ownerId) {
        return mindmapService.updateMindmapMemberPermission(mindmapId, userId, permission, ownerId);
    }

    @DeleteMapping("/{mindmapId}/members/{userId}")
    @Operation(summary = "Remove member from mindmap", description = "Owner removes a member from the mindmap")
    public Mono<Void> removeMindmapMember(
            @PathVariable Long mindmapId,
            @PathVariable Long userId,
            @RequestParam Long ownerId) {
        return mindmapService.removeMindmapMember(mindmapId, userId, ownerId);
    }
}
