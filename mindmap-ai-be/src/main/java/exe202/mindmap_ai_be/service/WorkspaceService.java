package exe202.mindmap_ai_be.service;

import exe202.mindmap_ai_be.entity.Workspace;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorkspaceService {
    Mono<Workspace> getWorkspaceById(Long id);
    Flux<Workspace> getAllWorkspaces();
    Mono<Workspace> createWorkspace(Workspace workspace);
    Mono<Workspace> updateWorkspace(Long id, Workspace workspace);
    Mono<Void> deleteWorkspace(Long id);
    Flux<Workspace> getWorkspacesByOwnerId(Long ownerId);
}
