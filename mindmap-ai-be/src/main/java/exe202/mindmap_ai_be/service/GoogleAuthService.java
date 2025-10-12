package exe202.mindmap_ai_be.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
import com.google.api.client.googleapis.util.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class GoogleAuthService {
    @Value("${google.client.id}")
    private String google_client_id;

    private final GoogleIdTokenVerifier verifier;

    public GoogleAuthService() {
        this.verifier = new GoogleIdTokenVerifier.Builder(
                new GooglePublicKeysManager.Builder(
                        Utils.getDefaultTransport(),
                        Utils.getDefaultJsonFactory()
                ).build()
        )
                .setAudience(Collections.singletonList("925746768505-b4bs779ll0nodni160a5uifq0njpngtb.apps.googleusercontent.com"))
                .build();
    }

    public Mono<GoogleIdToken.Payload> verify(String idTokenString) {
        return Mono.fromCallable(() -> {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload();
            }
            throw new IllegalArgumentException("Invalid Google ID Token");
        });
    }
}

