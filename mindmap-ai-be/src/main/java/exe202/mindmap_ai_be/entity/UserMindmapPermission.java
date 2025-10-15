package exe202.mindmap_ai_be.entity;

import exe202.mindmap_ai_be.entity.enums.MindmapPermission;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "user_mindmap_permissions")
@Data
@RequiredArgsConstructor
public class UserMindmapPermission {
    @Column("user_id")
    private Long userId;

    @Column("mindmap_id")
    private Long mindmapId;

    @Column("permission")
    private MindmapPermission permission;
}
