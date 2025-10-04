package exe202.mindmap_ai_be.entity;

import exe202.mindmap_ai_be.entity.enums.Shape;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name = "nodes")
@Data
@RequiredArgsConstructor
public class Node {
    @Id
    private Long nodeId;
    private Long mindMapId;
    private long parentNodeId;
    private String content;
    private float positionX;
    private float positionY;
    private String color;
    private Shape shape;
    private Timestamp createAt;
}
