package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User,Long> {

}

