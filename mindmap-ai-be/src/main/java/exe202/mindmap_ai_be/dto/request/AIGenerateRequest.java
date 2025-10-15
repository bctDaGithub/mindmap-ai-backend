package exe202.mindmap_ai_be.dto.request;

import exe202.mindmap_ai_be.entity.enums.LlmModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIGenerateRequest {
    private String prompt;
    private LlmModel llmModel = LlmModel.GEMINI;
}

