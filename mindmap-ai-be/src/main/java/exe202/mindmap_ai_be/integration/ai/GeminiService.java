package exe202.mindmap_ai_be.integration.ai;

import exe202.mindmap_ai_be.dto.response.AIMindmapResponse;
import org.springframework.stereotype.Service;

@Service
public interface GeminiService {
    AIMindmapResponse generateMindmap(String prompt);
}
