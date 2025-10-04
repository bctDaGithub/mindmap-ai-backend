package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.Payment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PaymentRepository extends ReactiveCrudRepository<Payment,Long> {
}
