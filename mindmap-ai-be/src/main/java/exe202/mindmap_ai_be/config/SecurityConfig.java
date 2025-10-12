package exe202.mindmap_ai_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // tắt CSRF cho API
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // TẮT httpBasic và formLogin — tránh Browser popup
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

                // TÙY CHỌN: custom entry point để trả 401 mà KHÔNG gửi header WWW-Authenticate
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((exchange, ex) -> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            // không set WWW-Authenticate header
                            return exchange.getResponse().setComplete();
                        })
                )

                // JWT filter ở vị trí authentication
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)

                // Quy tắc phân quyền
                .authorizeExchange(exchanges -> exchanges
                        // PERMIT ALL - FOR TESTING ONLY
                        .anyExchange().permitAll()
                )
//                .authorizeExchange(exchanges -> exchanges
//                        // public auth + swagger endpoints
//                        .pathMatchers(
//                                "/api/v1/auth/**",
//                                "/v3/api-docs/**",
//                                "/swagger-ui/**",
//                                "/swagger-ui.html",
//                                "/webjars/**",
//                                "/swagger-resources/**"
//                        ).permitAll()
//
//                        // WebSocket endpoints
//                        .pathMatchers("/ws/**").permitAll()
//
//                        // Public mindmap endpoints for testing
//                        .pathMatchers("/api/mindmap/**").permitAll()
//
//                        // Public workspace endpoints for testing
//                        .pathMatchers("/api/workspace/**", "/api/v1/workspaces/**").permitAll()
//
//                        // ví dụ endpoint admin
//                        .pathMatchers("/api/admin/**").hasAuthority("ADMIN")
//
//                        // các route còn lại require authenticated
//                        .anyExchange().authenticated()
//                )
                .build();
    }
}
