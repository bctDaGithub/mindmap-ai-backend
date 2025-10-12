package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.Edge;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EdgeRepository extends ReactiveCrudRepository<Edge,Long> {
    Flux<Edge> findAllByMindmapId(Long mindmapId);
}
