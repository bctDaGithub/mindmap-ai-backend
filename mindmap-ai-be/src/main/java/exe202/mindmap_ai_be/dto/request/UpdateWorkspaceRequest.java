package exe202.mindmap_ai_be.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to update an existing workspace")
public class UpdateWorkspaceRequest {

    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Schema(description = "Workspace name", example = "Updated Workspace Name")
    private String name;

    @Schema(description = "Workspace description", example = "Updated description")
    private String description;
}

