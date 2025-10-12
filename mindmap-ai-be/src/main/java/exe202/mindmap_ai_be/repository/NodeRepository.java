package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.Node;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface NodeRepository extends ReactiveCrudRepository<Node,Long> {
    Flux<Node> findAllByMindMapId(Long mindMapId);
}
