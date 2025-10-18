package exe202.mindmap_ai_be.config;


import exe202.mindmap_ai_be.jwt.JwtUtil;
import exe202.mindmap_ai_be.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtFilter implements WebFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // Không có token => cho qua (public route)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        // Validate token và authenticate, nếu lỗi thì CHO QUA thay vì trả 401
        // SecurityConfig sẽ quyết định có cần authentication hay không
        return jwtUtil.validateAndGetClaims(token)
                .flatMap(claims -> handleAuthentication(exchange, chain, claims))
                .onErrorResume(e -> {
                    // Token invalid/expired => CHỈ LOG, KHÔNG TRẢ 401
                    // Để SecurityConfig quyết định dựa trên permitAll() hay authenticated()
                    System.out.println("JWT validation failed: " + e.getMessage());
                    return chain.filter(exchange); // CHO QUA thay vì trả 401
                });
    }

    private Mono<Void> handleAuthentication(ServerWebExchange exchange, WebFilterChain chain, Claims claims) {
        String email = claims.getSubject();

        return userService.getUserByEmail(email)
                .flatMap(user -> {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user.getEmail(),
                                    null,
                                    Collections.singleton(() -> user.getRole())
                            );

                    SecurityContextImpl context = new SecurityContextImpl(authentication);

                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
                })
                .switchIfEmpty(chain.filter(exchange)); // user không tồn tại thì bỏ qua
    }

}
