package exe202.mindmap_ai_be.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdgeRepository extends ReactiveCrudRepository<EdgeRepository,Long> {
}
