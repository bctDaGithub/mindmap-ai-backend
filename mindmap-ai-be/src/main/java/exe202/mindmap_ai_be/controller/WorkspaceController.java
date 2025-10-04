package exe202.mindmap_ai_be.controller;


import exe202.mindmap_ai_be.entity.Workspace;
import exe202.mindmap_ai_be.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/workspaces")
@Tag(name = "Workspace", description = "API quản lý workspace")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Lấy thông tin workspace theo ID",
            description = "Trả về thông tin chi tiết của workspace dựa trên ID được cung cấp"
    )
    public Mono<Workspace> getWorkspace(@PathVariable  Long id) {
        return workspaceService.getWorkspaceById(id);
    }

}
