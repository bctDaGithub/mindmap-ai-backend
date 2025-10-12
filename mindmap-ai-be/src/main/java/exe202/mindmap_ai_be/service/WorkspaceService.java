package exe202.mindmap_ai_be.service;

import exe202.mindmap_ai_be.dto.request.CreateWorkspaceRequest;
import exe202.mindmap_ai_be.dto.request.UpdateWorkspaceRequest;
import exe202.mindmap_ai_be.dto.response.WorkspaceResponse;
import exe202.mindmap_ai_be.entity.Workspace;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorkspaceService {
    // CRUD operations with DTOs
    Mono<WorkspaceResponse> createWorkspace(CreateWorkspaceRequest request, Long ownerId);
    Mono<WorkspaceResponse> getWorkspaceById(Long id);
    Mono<WorkspaceResponse> updateWorkspace(Long id, UpdateWorkspaceRequest request, Long userId);
    Mono<Void> deleteWorkspace(Long id, Long userId);

    // Query operations
    Flux<WorkspaceResponse> getAllWorkspaces();
    Flux<WorkspaceResponse> getWorkspacesByOwnerId(Long ownerId);

    // Entity access for internal use
    Mono<Workspace> getWorkspaceEntityById(Long id);
}
