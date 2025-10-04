package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User,Long> {

}

