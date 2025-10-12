package exe202.mindmap_ai_be.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name = "workspaces")
@Data
@RequiredArgsConstructor
public class Workspace {

    @Id
    @Column("workspace_id")
    private Long workspaceId;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("owner_id")
    private Long ownerId;

    @Column("created_at")
    private Timestamp createdAt;
}
