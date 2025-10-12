package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.Workspace;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface WorkspaceRepository extends ReactiveCrudRepository<Workspace,Long> {
    Flux<Workspace> findAllByOwnerId(Long ownerId);
}
