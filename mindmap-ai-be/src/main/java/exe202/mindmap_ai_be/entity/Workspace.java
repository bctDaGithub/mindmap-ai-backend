package exe202.mindmap_ai_be.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name = "workspaces")
@Data
@RequiredArgsConstructor
public class Workspace {

    @Id
    private Long workspaceId;
    private String name;
    private String description;
    private Long owerId;
    private Timestamp createdAt;
}
