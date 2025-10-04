package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.AIGeneration;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AIGenerationRepository extends ReactiveCrudRepository<AIGeneration,Long> {
}
