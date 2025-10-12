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
@Schema(description = "Mindmap response data")
public class MindmapResponse {

    @Schema(description = "Mindmap ID", example = "1")
    private Long mindMapId;

    @Schema(description = "Mindmap title", example = "My Project Plan")
    private String title;

    @Schema(description = "Mindmap description", example = "A comprehensive project planning mindmap")
    private String description;

    @Schema(description = "Workspace ID", example = "1")
    private Long workspaceId;

    @Schema(description = "Owner user ID", example = "123")
    private Long ownerId;

    @Schema(description = "Is this mindmap public?", example = "false")
    private boolean isPublic;

    @Schema(description = "Creation timestamp")
    private Timestamp createdAt;

    @Schema(description = "Last update timestamp")
    private Timestamp updatedAt;
}

