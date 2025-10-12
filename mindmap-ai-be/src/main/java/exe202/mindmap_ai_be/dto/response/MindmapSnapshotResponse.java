package exe202.mindmap_ai_be.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Snapshot response containing all nodes and edges in a mindmap")
public class MindmapSnapshotResponse {

    @Schema(description = "List of all nodes in the mindmap")
    private List<NodeDTO> nodes;

    @Schema(description = "List of all edges in the mindmap")
    private List<EdgeDTO> edges;

    @Schema(description = "Total number of active users")
    private Integer activeUsers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Node data transfer object")
    public static class NodeDTO {
        private Long nodeId;
        private Long mindMapId;
        private Long parentNodeId;
        private String content;
        private Double positionX;
        private Double positionY;
        private String color;
        private String shape;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Edge data transfer object")
    public static class EdgeDTO {
        private Long id;
        private Long mindmapId;
        private Long fromNodeId;
        private Long toNodeId;
        private String label;
    }
}

