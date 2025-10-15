package exe202.mindmap_ai_be.service.impl;

import exe202.mindmap_ai_be.dto.request.CreateWorkspaceRequest;
import exe202.mindmap_ai_be.dto.request.UpdateWorkspaceRequest;
import exe202.mindmap_ai_be.dto.response.WorkspaceResponse;
import exe202.mindmap_ai_be.entity.Workspace;
import exe202.mindmap_ai_be.entity.WorkspaceMember;
import exe202.mindmap_ai_be.entity.enums.WorkspacePermission;
import exe202.mindmap_ai_be.repository.WorkspaceMemberRepository;
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
    private final WorkspaceMemberRepository workspaceMemberRepository;

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

    // ============= Collaboration Operations Implementation =============

    @Override
    public Mono<Void> inviteUserToWorkspace(Long workspaceId, Long targetUserId, String permission, Long ownerId) {
        return workspaceRepository.findById(workspaceId)
                .flatMap(workspace -> {
                    // Check if requester is the owner
                    if (!workspace.getOwnerId().equals(ownerId)) {
                        return Mono.error(new RuntimeException("Only owner can invite users to workspace"));
                    }

                    // Check if user already has membership
                    return workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, targetUserId)
                            .hasElement()
                            .flatMap(exists -> {
                                if (exists) {
                                    return Mono.error(new RuntimeException("User is already a member of this workspace"));
                                }

                                // Create new member
                                WorkspaceMember newMember = new WorkspaceMember();
                                newMember.setWorkspaceId(workspaceId);
                                newMember.setUserId(targetUserId);

                                // Parse permission string to enum
                                WorkspacePermission permEnum;
                                if ("EDIT".equalsIgnoreCase(permission)) {
                                    permEnum = WorkspacePermission.EDIT;
                                } else if ("VIEW_ONLY".equalsIgnoreCase(permission) || "VIEW".equalsIgnoreCase(permission)) {
                                    permEnum = WorkspacePermission.VIEW_ONLY;
                                } else {
                                    permEnum = WorkspacePermission.VIEW_ONLY; // Default to VIEW_ONLY
                                }
                                newMember.setWorkspacePermission(permEnum);

                                return workspaceMemberRepository.save(newMember).then();
                            });
                });
    }

    @Override
    public Flux<WorkspaceMember> getWorkspaceMembers(Long workspaceId) {
        return workspaceMemberRepository.findByWorkspaceId(workspaceId);
    }

    @Override
    public Mono<Void> updateWorkspaceMemberPermission(Long workspaceId, Long userId, String permission, Long ownerId) {
        return workspaceRepository.findById(workspaceId)
                .flatMap(workspace -> {
                    // Check if requester is the owner
                    if (!workspace.getOwnerId().equals(ownerId)) {
                        return Mono.error(new RuntimeException("Only owner can update permissions"));
                    }

                    return workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, userId)
                            .switchIfEmpty(Mono.error(new RuntimeException("User is not a member of this workspace")))
                            .flatMap(existingMember -> {
                                // Update permission
                                WorkspacePermission permEnum;
                                if ("EDIT".equalsIgnoreCase(permission)) {
                                    permEnum = WorkspacePermission.EDIT;
                                } else if ("VIEW_ONLY".equalsIgnoreCase(permission) || "VIEW".equalsIgnoreCase(permission)) {
                                    permEnum = WorkspacePermission.VIEW_ONLY;
                                } else {
                                    permEnum = WorkspacePermission.VIEW_ONLY; // Default to VIEW_ONLY
                                }
                                existingMember.setWorkspacePermission(permEnum);

                                return workspaceMemberRepository.save(existingMember).then();
                            });
                });
    }

    @Override
    public Mono<Void> removeWorkspaceMember(Long workspaceId, Long userId, Long ownerId) {
        return workspaceRepository.findById(workspaceId)
                .flatMap(workspace -> {
                    // Check if requester is the owner
                    if (!workspace.getOwnerId().equals(ownerId)) {
                        return Mono.error(new RuntimeException("Only owner can remove members"));
                    }

                    // Cannot remove owner
                    if (workspace.getOwnerId().equals(userId)) {
                        return Mono.error(new RuntimeException("Cannot remove owner from workspace"));
                    }

                    return workspaceMemberRepository.deleteByWorkspaceIdAndUserId(workspaceId, userId);
                });
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
