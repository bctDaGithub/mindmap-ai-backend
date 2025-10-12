package exe202.mindmap_ai_be.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Workspace response data")
public class WorkspaceResponse {

    @Schema(description = "Workspace ID", example = "1")
    private Long workspaceId;

    @Schema(description = "Workspace name", example = "My Team Workspace")
    private String name;

    @Schema(description = "Workspace description", example = "Collaborative workspace for our team")
    private String description;

    @Schema(description = "Owner user ID", example = "123")
    private Long ownerId;

    @Schema(description = "Creation timestamp")
    private Timestamp createdAt;
}

