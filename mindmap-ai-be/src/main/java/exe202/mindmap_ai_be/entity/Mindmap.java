package exe202.mindmap_ai_be.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name = "mindmaps")
@Data
@RequiredArgsConstructor
public class Mindmap {
    @Id
    @Column("mind_map_id")
    private Long mindMapId;

    @NotBlank
    @Size(min = 5, max = 60)
    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @Column("workspace_id")
    private Long workspaceId;

    @Column("owner_id")
    private Long ownerId;

    @Column("is_public")
    private boolean isPublic;

    @Column("created_at")
    private Timestamp createdAt;

    @Column("updated_at")
    private Timestamp updatedAt;

}

