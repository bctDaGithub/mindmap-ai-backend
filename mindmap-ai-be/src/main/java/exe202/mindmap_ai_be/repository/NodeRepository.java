package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.Node;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface NodeRepository extends ReactiveCrudRepository<Node,Long> {

}
