package exe202.mindmap_ai_be.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name = "mindmaps")
@Data
@RequiredArgsConstructor
public class Mindmap {
    @Id
    private Long mindMapId;

    @NotBlank
    @Size(min = 5, max = 60)
    private String title;



    private String description;
    private Long workspaceId;
    private Long ownerId;
    private boolean isPublic;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}

