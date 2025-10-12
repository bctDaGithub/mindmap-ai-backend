package exe202.mindmap_ai_be.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "edges")
@Data
@RequiredArgsConstructor
public class Edge {
    @Id
    @Column("edge_id")
    private Long id;

    @Column("mind_map_id")
    private Long mindmapId;

    @Column("from_node_id")
    private Long fromNodeId;
    @Column("to_node_id")
    private Long toNodeId;
    @Column("label")
    private String label;
}
