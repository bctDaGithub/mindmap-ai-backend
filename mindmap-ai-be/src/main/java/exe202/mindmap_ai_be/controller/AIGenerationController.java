package exe202.mindmap_ai_be.controller;

import exe202.mindmap_ai_be.dto.request.AIGenerateRequest;
import exe202.mindmap_ai_be.dto.response.AIMindmapResponse;
import exe202.mindmap_ai_be.entity.enums.LlmModel;
import exe202.mindmap_ai_be.service.AIGenerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Tag(name = "AI Generation", description = "AI-powered mindmap generation endpoints")
public class AIGenerationController {

    private final AIGenerationService aiGenerationService;

    @PostMapping("/generate-mindmap")
    @Operation(summary = "Generate mindmap from prompt",
               description = "Uses AI to generate a mindmap structure (nodes and edges) based on user's prompt. Returns a preview that can be used by frontend before creating the actual mindmap.")
    public Mono<ResponseEntity<AIMindmapResponse>> generateMindmap(
            @RequestBody AIGenerateRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        LlmModel model = request.getLlmModel() != null ? request.getLlmModel() : LlmModel.GEMINI;

        return aiGenerationService.generateMindmap(userId, request.getPrompt(), model)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }
}
