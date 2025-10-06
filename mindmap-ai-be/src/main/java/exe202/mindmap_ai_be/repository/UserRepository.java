package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User,Long> {
    Mono<User> findByEmail(String email);
}

