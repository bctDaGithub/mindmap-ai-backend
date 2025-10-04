package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.WorkspaceMember;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface WorkspaceMemberRepository extends ReactiveCrudRepository<WorkspaceMember,Long> {
}
