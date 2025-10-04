package exe202.mindmap_ai_be.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WebFlux Reactive API")
                        .version("1.0.0")
                        .description("API documentation for Mindmap AI Backend")
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                );
    }
}