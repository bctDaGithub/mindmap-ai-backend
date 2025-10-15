package exe202.mindmap_ai_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIMindmapResponse {
    private String title;
    private List<AINodeResponse> nodes;
    private List<AIEdgeResponse> edges;
}

