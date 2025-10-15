package exe202.mindmap_ai_be.controller;

import exe202.mindmap_ai_be.dto.request.CreateWorkspaceRequest;
import exe202.mindmap_ai_be.dto.request.UpdateWorkspaceRequest;
import exe202.mindmap_ai_be.dto.response.WorkspaceResponse;
import exe202.mindmap_ai_be.entity.WorkspaceMember;
import exe202.mindmap_ai_be.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/workspaces")
@Tag(name = "Workspace", description = "API quản lý workspace")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Tạo workspace mới", description = "Tạo một workspace mới cho việc cộng tác trong nhóm")
    public Mono<WorkspaceResponse> createWorkspace(
            @Valid @RequestBody CreateWorkspaceRequest request,
            @Parameter(description = "ID người dùng sở hữu") @RequestParam(required = false, defaultValue = "1") Long ownerId) {
        return workspaceService.createWorkspace(request, ownerId);
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả workspace", description = "Lấy tất cả các workspace trong hệ thống")
    public Flux<WorkspaceResponse> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('USER')")
    @Operation(
            summary = "Lấy thông tin workspace theo ID",
            description = "Trả về thông tin chi tiết của workspace dựa trên ID được cung cấp"
    )
    public Mono<WorkspaceResponse> getWorkspaceById(
            @Parameter(description = "ID workspace") @PathVariable Long id) {
        return workspaceService.getWorkspaceById(id);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Lấy workspace theo chủ sở hữu", description = "Lấy tất cả các workspace do một người dùng cụ thể sở hữu")
    public Flux<WorkspaceResponse> getWorkspacesByOwnerId(
            @Parameter(description = "ID người dùng sở hữu") @PathVariable Long ownerId) {
        return workspaceService.getWorkspacesByOwnerId(ownerId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật workspace", description = "Cập nhật thông tin của một workspace đã tồn tại")
    public Mono<WorkspaceResponse> updateWorkspace(
            @Parameter(description = "ID workspace") @PathVariable Long id,
            @Valid @RequestBody UpdateWorkspaceRequest request,
            @Parameter(description = "ID người dùng") @RequestParam(required = false, defaultValue = "1") Long userId) {
        return workspaceService.updateWorkspace(id, request, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Xóa workspace", description = "Xóa một workspace và tất cả dữ liệu liên quan")
    public Mono<Void> deleteWorkspace(
            @Parameter(description = "ID workspace") @PathVariable Long id,
            @Parameter(description = "ID người dùng") @RequestParam(required = false, defaultValue = "1") Long userId) {
        return workspaceService.deleteWorkspace(id, userId);
    }

    // ============= Collaboration Endpoints =============

    @PostMapping("/{workspaceId}/invite")
    @Operation(summary = "Mời người dùng vào workspace", description = "Owner mời người dùng khác để cùng cộng tác trong workspace")
    public Mono<Void> inviteUserToWorkspace(
            @Parameter(description = "ID workspace") @PathVariable Long workspaceId,
            @Parameter(description = "ID người dùng được mời") @RequestParam Long targetUserId,
            @Parameter(description = "Quyền: EDIT hoặc VIEW_ONLY") @RequestParam String permission,
            @Parameter(description = "ID owner") @RequestParam Long ownerId) {
        return workspaceService.inviteUserToWorkspace(workspaceId, targetUserId, permission, ownerId);
    }

    @GetMapping("/{workspaceId}/members")
    @Operation(summary = "Lấy danh sách thành viên workspace", description = "Xem tất cả thành viên đang cộng tác trong workspace")
    public Flux<WorkspaceMember> getWorkspaceMembers(
            @Parameter(description = "ID workspace") @PathVariable Long workspaceId) {
        return workspaceService.getWorkspaceMembers(workspaceId);
    }

    @PutMapping("/{workspaceId}/members/{userId}/permission")
    @Operation(summary = "Cập nhật quyền thành viên", description = "Owner cập nhật quyền của thành viên trong workspace")
    public Mono<Void> updateWorkspaceMemberPermission(
            @Parameter(description = "ID workspace") @PathVariable Long workspaceId,
            @Parameter(description = "ID thành viên") @PathVariable Long userId,
            @Parameter(description = "Quyền mới: EDIT hoặc VIEW_ONLY") @RequestParam String permission,
            @Parameter(description = "ID owner") @RequestParam Long ownerId) {
        return workspaceService.updateWorkspaceMemberPermission(workspaceId, userId, permission, ownerId);
    }

    @DeleteMapping("/{workspaceId}/members/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Xóa thành viên khỏi workspace", description = "Owner xóa một thành viên ra khỏi workspace")
    public Mono<Void> removeWorkspaceMember(
            @Parameter(description = "ID workspace") @PathVariable Long workspaceId,
            @Parameter(description = "ID thành viên") @PathVariable Long userId,
            @Parameter(description = "ID owner") @RequestParam Long ownerId) {
        return workspaceService.removeWorkspaceMember(workspaceId, userId, ownerId);
    }
}
