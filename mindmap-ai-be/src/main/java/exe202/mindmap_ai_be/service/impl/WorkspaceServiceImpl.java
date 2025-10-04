package exe202.mindmap_ai_be.service.impl;

import exe202.mindmap_ai_be.entity.Workspace;
import exe202.mindmap_ai_be.repository.WorkspaceRepository;
import exe202.mindmap_ai_be.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    @Override
    public Mono<Workspace> getWorkspaceById(Long id) {
        return workspaceRepository.findById(id);
    }

    @Override
    public Flux<Workspace> getAllWorkspaces() {
        return workspaceRepository.findAll();
    }

    @Override
    public Mono<Workspace> createWorkspace(Workspace workspace) {
        return workspaceRepository.save(workspace);
    }

    @Override
    public Mono<Workspace> updateWorkspace(Long id, Workspace workspace) {
        Workspace currentWorkspace = workspaceRepository.findById(id).block();
        currentWorkspace.setName(workspace.getName());
        currentWorkspace.setDescription(workspace.getDescription());
        return workspaceRepository.save(currentWorkspace);
    }

    @Override
    public Mono<Void> deleteWorkspace(Long id) {
        return workspaceRepository.deleteById(id);
    }

    @Override
    public Flux<Workspace> getWorkspacesByOwnerId(Long ownerId) {
        return workspaceRepository.getWorkspacesByOwerId(ownerId);
    }


}
