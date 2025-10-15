package exe202.mindmap_ai_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIEdgeResponse {
    private Long fromNodeId;
    private Long toNodeId;
    private String label;
}

