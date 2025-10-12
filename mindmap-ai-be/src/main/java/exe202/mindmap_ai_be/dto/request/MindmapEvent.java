package exe202.mindmap_ai_be.dto.request;

import exe202.mindmap_ai_be.entity.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MindmapEvent {
    private EventType eventType;
    private Long mindMapId;
    private Long userId;
    private String entity; // "node" or "edge"
    private Map<String, Object> payload;
    private Instant timestamp = Instant.now();

    public MindmapEvent(EventType eventType, Long mindMapId, Long userId, String entity, Map<String, Object> payload) {
        this.eventType = eventType;
        this.mindMapId = mindMapId;
        this.userId = userId;
        this.entity = entity;
        this.payload = payload;
        this.timestamp = Instant.now();
    }
}
