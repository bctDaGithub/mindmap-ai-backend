package exe202.mindmap_ai_be.service;

import exe202.mindmap_ai_be.dto.response.AIMindmapResponse;
import exe202.mindmap_ai_be.entity.enums.LlmModel;
import reactor.core.publisher.Mono;

public interface AIGenerationService {
    Mono<AIMindmapResponse> generateMindmap(Long userId, String prompt, LlmModel llmModel);
}
