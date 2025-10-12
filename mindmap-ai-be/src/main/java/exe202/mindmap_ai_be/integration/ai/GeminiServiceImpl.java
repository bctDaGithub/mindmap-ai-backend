package exe202.mindmap_ai_be.integration.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;

public class GeminiServiceImpl implements  GeminiService {

    @Value("${GOOGLE_API_KEY}")
    private String apiKey;

    Client client = new Client();

    public String generateMindmap(String prompt) {
        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        "Giải thích AI là gì?",
                        null );
        System.out.println(response.text());
        return response.text();
    }
}
