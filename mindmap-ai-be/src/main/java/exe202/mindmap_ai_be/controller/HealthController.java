package exe202.mindmap_ai_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health Check", description = "API để kiểm tra trạng thái hệ thống")
public class HealthController {

    @GetMapping
    @Operation(
            summary = "Kiểm tra trạng thái server",
            description = "Endpoint này trả về trạng thái hoạt động của server và thông tin về thời gian"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Server đang hoạt động bình thường"),
            @ApiResponse(responseCode = "500", description = "Server gặp lỗi")
    })
    public Mono<ResponseEntity<Map<String, Object>>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Mindmap AI Backend is running");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "mindmap-ai-be");

        return Mono.just(ResponseEntity.ok(response));
    }

    @GetMapping("/ping")
    @Operation(
            summary = "Ping server",
            description = "Endpoint đơn giản để kiểm tra kết nối"
    )
    @ApiResponse(responseCode = "200", description = "Pong response")
    public Mono<ResponseEntity<String>> ping() {
        return Mono.just(ResponseEntity.ok("pong"));
    }
}

