package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.Workspace;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface WorkspaceRepository extends ReactiveCrudRepository<Workspace,Long> {
}
