package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.Mindmap;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MindmapRepository extends ReactiveCrudRepository<Mindmap,Long> {
}
