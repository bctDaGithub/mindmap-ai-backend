package exe202.mindmap_ai_be.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to update an existing mindmap")
public class UpdateMindmapRequest {

    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    @Schema(description = "Mindmap title", example = "Updated Project Plan")
    private String title;

    @Schema(description = "Mindmap description", example = "Updated description")
    private String description;

    @Schema(description = "Workspace ID", example = "1")
    private Long workspaceId;

    @Schema(description = "Is this mindmap public?", example = "true")
    private Boolean isPublic;
}

