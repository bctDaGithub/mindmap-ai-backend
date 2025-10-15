package exe202.mindmap_ai_be.service.impl;

import exe202.mindmap_ai_be.dto.response.AIMindmapResponse;
import exe202.mindmap_ai_be.entity.AIGeneration;
import exe202.mindmap_ai_be.entity.enums.LlmModel;
import exe202.mindmap_ai_be.integration.ai.GeminiService;
import exe202.mindmap_ai_be.repository.AIGenerationRepository;
import exe202.mindmap_ai_be.service.AIGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIGenerationServiceImpl implements AIGenerationService {

    private final GeminiService geminiService;
    private final AIGenerationRepository aiGenerationRepository;

    @Override
    public Mono<AIMindmapResponse> generateMindmap(Long userId, String prompt, LlmModel llmModel) {
        return Mono.fromCallable(() -> {
            // Generate mindmap using AI
            AIMindmapResponse response;
            switch (llmModel) {
                case GEMINI -> {
                    response = geminiService.generateMindmap(prompt);
                }
                default -> throw new IllegalArgumentException("Unsupported LLM model: " + llmModel);
            }
            return response;
        })
        .flatMap(response -> {
            // Save AI generation record to database
            AIGeneration aiGeneration = new AIGeneration();
            aiGeneration.setUserId(userId);
            aiGeneration.setInputText(prompt);
            aiGeneration.setOutputSummary(response.getTitle() + " - " + response.getNodes().size() + " nodes");
            aiGeneration.setModelUsed(llmModel);
            aiGeneration.setCreatedAt(Timestamp.from(Instant.now()));

            return aiGenerationRepository.save(aiGeneration)
                    .doOnSuccess(saved -> log.info("Saved AI generation record with ID: {}", saved.getGenerationId()))
                    .thenReturn(response);
        })
        .doOnError(error -> log.error("Error generating mindmap: ", error));
    }
}
