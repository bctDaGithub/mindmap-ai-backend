package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.Subscription;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SubscriptionRepository extends ReactiveCrudRepository<Subscription,Long> {
}
