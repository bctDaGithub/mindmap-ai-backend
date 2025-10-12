package exe202.mindmap_ai_be.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mindmap AI Backend API")
                        .version("1.0.0")
                        .description("""
                                API documentation for Mindmap AI Backend - A reactive WebFlux application with real-time collaboration.
                                
                                ## Features
                                - **Real-time Collaboration**: WebSocket endpoint at `ws://localhost:8080/ws/mindmap?mindmapId={id}&userId={userId}`
                                - **RESTful APIs**: Complete CRUD operations for nodes and edges
                                - **Reactive**: Built with Spring WebFlux for high performance
                                
                                ## WebSocket Events
                                Supported event types:
                                - Node: ADD_NODE, UPDATE_NODE, DELETE_NODE, MOVE_NODE, UPDATE_NODE_COLOR, UPDATE_NODE_SHAPE, UPDATE_NODE_CONTENT
                                - Edge: ADD_EDGE, UPDATE_EDGE, DELETE_EDGE, UPDATE_EDGE_LABEL
                                - User: USER_JOIN, USER_LEAVE, USER_CURSOR_MOVE
                                - Sync: SYNC_REQUEST, SYNC_SNAPSHOT
                                """)
                        .contact(new Contact()
                                .name("Mindmap AI Team")
                                .email("support@mindmap-ai.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.mindmap-ai.com")
                                .description("Production Server")
                ))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }
}