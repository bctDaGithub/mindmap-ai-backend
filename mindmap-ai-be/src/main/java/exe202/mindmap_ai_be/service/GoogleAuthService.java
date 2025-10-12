package exe202.mindmap_ai_be.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import reactor.core.publisher.Mono;

public interface GoogleAuthService {
    Mono<GoogleIdToken.Payload> verify(String idTokenString);
}
