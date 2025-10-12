package exe202.mindmap_ai_be.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.UnknownHostException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseConnectionTest {

    private final ConnectionFactory connectionFactory;

    @EventListener(ApplicationReadyEvent.class)
    public void testConnection() {
        Mono.from(connectionFactory.create())
                .timeout(Duration.ofSeconds(10))
                .flatMap(connection ->
                        Mono.from(connection.createStatement("SELECT NOW() AS current_time").execute())
                                .flatMap(result ->
                                        Mono.from(result.map((row, meta) -> row.get("current_time", String.class))))
                                .doOnNext(time -> log.info("✅ Database connected successfully! Server time: {}", time))
                                .doFinally(signalType -> connection.close())
                )
                .doOnError(this::handleError)
                .onErrorResume(err -> Mono.empty())
                .subscribe();
    }

    private void handleError(Throwable err) {
        log.error("❌ Database connection failed: {}", err.getMessage());

        if (err instanceof UnknownHostException) {
            log.error("🔍 Nguyên nhân: Không phân giải được tên miền (DNS). Kiểm tra lại host hoặc mạng Internet.");
        } else if (err.getMessage() != null && err.getMessage().contains("Connection refused")) {
            log.error("🔍 Nguyên nhân: Supabase từ chối kết nối. Có thể sai port, user/password hoặc server đang down.");
        } else if (err.getMessage() != null && err.getMessage().contains("SSL")) {
            log.error("🔍 Nguyên nhân: Thiếu SSL hoặc sai cấu hình. Thêm '?sslMode=require' vào URL R2DBC.");
        } else if (err instanceof TimeoutException) {
            log.error("🔍 Nguyên nhân: Kết nối bị timeout (Supabase có thể chặn IP này).");
        } else {
            log.error("🔍 Chi tiết đầy đủ lỗi:", err);
        }
    }
}
