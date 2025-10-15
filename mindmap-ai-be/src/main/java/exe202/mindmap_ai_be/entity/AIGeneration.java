package exe202.mindmap_ai_be.entity;

import exe202.mindmap_ai_be.entity.enums.LlmModel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name = "ai_generations")
@Data
@RequiredArgsConstructor
public class AIGeneration {
    @Id
    @Column("generation_id")
    private Long generationId;

    @Column("mindmap_id")
    private Long mindmapId;

    @Column("user_id")
    private Long userId;

    @Column("input_text")
    private String inputText;

    @Column("output_summary")
    private String outputSummary;

    @Column("model_used")
    private LlmModel modelUsed;

    @Column("created_at")
    private Timestamp createdAt;
}
