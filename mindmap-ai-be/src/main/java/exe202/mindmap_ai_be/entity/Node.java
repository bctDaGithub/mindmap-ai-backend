package exe202.mindmap_ai_be.entity;

import exe202.mindmap_ai_be.entity.enums.Shape;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "nodes")
@Data
@RequiredArgsConstructor
public class Node {
    @Id
    @Column("node_id")
    private Long nodeId;

    @Column("mind_map_id")
    private Long mindMapId;

    @Column("parent_node_id")
    private Long parentNodeId;

    @Column("content")
    private String content;

    @Column("position_x")
    private double positionX;

    @Column("position_y")
    private double positionY;

    @Column("color")
    private String color;

    @Column("shape")
    private Shape shape;

    @Column("created_at")
    private Instant createAt;

    @Version
    private Long version;
}
