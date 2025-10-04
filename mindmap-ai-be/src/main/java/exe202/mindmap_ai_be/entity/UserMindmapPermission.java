package exe202.mindmap_ai_be.entity;

import exe202.mindmap_ai_be.entity.enums.MindmapPermission;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "user_mindmap_permissions")
@Data
@RequiredArgsConstructor
public class UserMindmapPermission {
    private Long userId;
    private Long mindmapId;
    private MindmapPermission permission;
}
