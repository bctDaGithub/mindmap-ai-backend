package exe202.mindmap_ai_be.entity;

import exe202.mindmap_ai_be.entity.enums.WorkspacePermission;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "workspace_members")
@Data
@RequiredArgsConstructor
public class WorkspaceMember {
    @Column("workspace_id")
    private Long workspaceId;

    @Column("user_id")
    private Long userId;

    @Column("workspace_permission")
    private WorkspacePermission workspacePermission;
}
