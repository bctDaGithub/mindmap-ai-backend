package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.UserMindmapPermission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserMindmapPermissionRepository extends ReactiveCrudRepository<UserMindmapPermission,Long> {}