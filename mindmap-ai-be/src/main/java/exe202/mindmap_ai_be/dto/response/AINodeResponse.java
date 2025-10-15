package exe202.mindmap_ai_be.dto.response;

import exe202.mindmap_ai_be.entity.enums.Shape;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AINodeResponse {
    private Long nodeId;  // Temporary ID for preview (used by edges)
    private String content;
    private Long parentNodeId;
    private double positionX;
    private double positionY;
    private String color;
    private Shape shape;
}
