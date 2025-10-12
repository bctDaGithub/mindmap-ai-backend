package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.Mindmap;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MindmapRepository extends ReactiveCrudRepository<Mindmap,Long> {
    Flux<Mindmap> findAllByOwnerId(Long ownerId);
    Flux<Mindmap> findAllByWorkspaceId(Long workspaceId);
    Flux<Mindmap> findAllByIsPublic(boolean isPublic);
}
