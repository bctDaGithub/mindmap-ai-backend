package exe202.mindmap_ai_be.service.impl;

import exe202.mindmap_ai_be.dto.request.CreateWorkspaceRequest;
import exe202.mindmap_ai_be.dto.request.UpdateWorkspaceRequest;
import exe202.mindmap_ai_be.dto.response.WorkspaceResponse;
import exe202.mindmap_ai_be.entity.Workspace;
import exe202.mindmap_ai_be.repository.WorkspaceRepository;
import exe202.mindmap_ai_be.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    @Override
    public Mono<WorkspaceResponse> createWorkspace(CreateWorkspaceRequest request, Long ownerId) {
        Workspace workspace = new Workspace();
        workspace.setName(request.getName());
        workspace.setDescription(request.getDescription());
        workspace.setOwnerId(ownerId);
        workspace.setCreatedAt(Timestamp.from(Instant.now()));

        return workspaceRepository.save(workspace)
                .map(this::toWorkspaceResponse);
    }

    @Override
    public Mono<WorkspaceResponse> getWorkspaceById(Long id) {
        return workspaceRepository.findById(id)
                .map(this::toWorkspaceResponse);
    }

    @Override
    public Mono<Workspace> getWorkspaceEntityById(Long id) {
        return workspaceRepository.findById(id);
    }

    @Override
    public Mono<WorkspaceResponse> updateWorkspace(Long id, UpdateWorkspaceRequest request, Long userId) {
        return workspaceRepository.findById(id)
                .flatMap(workspace -> {
                    // Optional: Add ownership check
                    if (request.getName() != null) {
                        workspace.setName(request.getName());
                    }
                    if (request.getDescription() != null) {
                        workspace.setDescription(request.getDescription());
                    }
                    return workspaceRepository.save(workspace);
                })
                .map(this::toWorkspaceResponse);
    }

    @Override
    public Mono<Void> deleteWorkspace(Long id, Long userId) {
        return workspaceRepository.findById(id)
                .flatMap(workspace -> {
                    // Optional: Add ownership check
                    return workspaceRepository.deleteById(id);
                });
    }

    @Override
    public Flux<WorkspaceResponse> getAllWorkspaces() {
        return workspaceRepository.findAll()
                .map(this::toWorkspaceResponse);
    }

    @Override
    public Flux<WorkspaceResponse> getWorkspacesByOwnerId(Long ownerId) {
        return workspaceRepository.findAllByOwnerId(ownerId)
                .map(this::toWorkspaceResponse);
    }

    // Helper method
    private WorkspaceResponse toWorkspaceResponse(Workspace workspace) {
        return WorkspaceResponse.builder()
                .workspaceId(workspace.getWorkspaceId())
                .name(workspace.getName())
                .description(workspace.getDescription())
                .ownerId(workspace.getOwnerId())
                .createdAt(workspace.getCreatedAt())
                .build();
    }
}
