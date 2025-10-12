package exe202.mindmap_ai_be.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new mindmap")
public class CreateMindmapRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    @Schema(description = "Mindmap title", example = "My Project Plan")
    private String title;

    @Schema(description = "Mindmap description", example = "A comprehensive project planning mindmap")
    private String description;

    @Schema(description = "Workspace ID (optional)", example = "1")
    private Long workspaceId;

    @Schema(description = "Is this mindmap public?", example = "false")
    private boolean isPublic = false;
}

