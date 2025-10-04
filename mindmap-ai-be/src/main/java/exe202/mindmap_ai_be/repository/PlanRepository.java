package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.Plan;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends ReactiveCrudRepository<Plan,Long> {
}
