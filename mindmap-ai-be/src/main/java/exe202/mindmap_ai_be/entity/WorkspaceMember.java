package exe202.mindmap_ai_be.entity;

import exe202.mindmap_ai_be.entity.enums.WorkspacePermission;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "workspace_members")
@Data
@RequiredArgsConstructor
public class WorkspaceMember {
    private Long workspaceId;
    private Long userId;
    private WorkspacePermission workspacePermission;
}
