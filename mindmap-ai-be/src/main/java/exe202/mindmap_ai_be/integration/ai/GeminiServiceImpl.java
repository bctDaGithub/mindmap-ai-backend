package exe202.mindmap_ai_be.integration.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import exe202.mindmap_ai_be.dto.response.AIMindmapResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GeminiServiceImpl implements GeminiService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AIMindmapResponse generateMindmap(String prompt) {
        try {
            Client client = new Client();

            String systemPrompt = buildSystemPrompt();
            String fullPrompt = systemPrompt + "\n\nUser request: " + prompt;

            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    fullPrompt,
                    null
            );

            String responseText = response.text();
            log.info("Gemini response: {}", responseText);

            // Parse JSON response
            String jsonContent = extractJsonFromResponse(responseText);
            AIMindmapResponse mindmapResponse = objectMapper.readValue(jsonContent, AIMindmapResponse.class);

            return mindmapResponse;

        } catch (Exception e) {
            log.error("Error generating mindmap with Gemini: ", e);
            throw new RuntimeException("Failed to generate mindmap: " + e.getMessage());
        }
    }

    private String buildSystemPrompt() {
        return """
                You are a mindmap generation assistant. Generate a hierarchical mindmap structure based on user's prompt.
                
                IMPORTANT: Return ONLY valid JSON, no markdown formatting, no code blocks, no explanations.
                
                Rules:
                1. Create a root node (parentNodeId: null) as the main topic
                2. Create child nodes hierarchically
                3. Use nodeId as temporary identifier starting from 0 (this is for preview only, will be assigned real IDs when saved)
                4. Position nodes in a tree layout (root at center, children spread out)
                5. Assign appropriate colors (hex format like #4A90E2, #F39C12, #27AE60, #E74C3C, #9B59B6, #E67E22)
                6. Use shape: RECTANGLE for root, ELLIPSE for level 1, CIRCLE for deeper levels
                7. Keep content concise and clear
                8. Create edges connecting parent to child nodes using the nodeId references
                
                Response format:
                {
                  "title": "Mindmap Title",
                  "nodes": [
                    {
                      "nodeId": 0,
                      "content": "Root Node",
                      "parentNodeId": null,
                      "positionX": 0,
                      "positionY": 0,
                      "color": "#4A90E2",
                      "shape": "RECTANGLE"
                    },
                    {
                      "nodeId": 1,
                      "content": "Child Node",
                      "parentNodeId": 0,
                      "positionX": -200,
                      "positionY": 150,
                      "color": "#E67E22",
                      "shape": "ELLIPSE"
                    }
                  ],
                  "edges": [
                    {
                      "fromNodeId": 0,
                      "toNodeId": 1,
                      "label": "relation"
                    }
                  ]
                }
                
                Valid shapes: RECTANGLE, CIRCLE, ELLIPSE, DIAMOND, HEXAGON, OCTAGON, PARALLELOGRAM, TRAPEZOID, STAR, CLOUD
                
                Position guide:
                - Root node: (0, 0)
                - Level 1 children: spread horizontally around root (e.g., -300 to 300 on X-axis, Y around 150)
                - Level 2+: further spread from parent nodes (increase X range and Y distance)
                - Keep reasonable spacing so nodes don't overlap
                
                Return ONLY the JSON object, nothing else. No markdown, no explanations.
                """;
    }

    private String extractJsonFromResponse(String response) {
        // Remove markdown code blocks if present
        String cleaned = response.trim();
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }
        return cleaned.trim();
    }
}
