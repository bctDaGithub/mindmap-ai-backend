package exe202.mindmap_ai_be.entity;

import exe202.mindmap_ai_be.entity.enums.LlmModel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name = "ai_generations")
@Data
@RequiredArgsConstructor
public class AIGeneration {
    private Long generationId;
    private Long mindmapId;
    private Long userId;
    private String inputText;
    private String outputSummary;
    private LlmModel modelUsed;
    private Timestamp createdAt;
}
