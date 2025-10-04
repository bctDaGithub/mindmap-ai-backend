package exe202.mindmap_ai_be.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "edges")
@Data
@RequiredArgsConstructor
public class Edge {
    private Long fromNodeId;
    private Long toNodeId;
    private String label;
}
