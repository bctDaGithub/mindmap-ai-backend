package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.UserMindmapPermission;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserMindmapPermissionRepository extends ReactiveCrudRepository<UserMindmapPermission,Long> {

    @Query("SELECT * FROM user_mindmap_permissions WHERE mindmap_id = :mindmapId")
    Flux<UserMindmapPermission> findByMindmapId(Long mindmapId);

    @Query("SELECT * FROM user_mindmap_permissions WHERE mindmap_id = :mindmapId AND user_id = :userId")
    Mono<UserMindmapPermission> findByMindmapIdAndUserId(Long mindmapId, Long userId);

    @Query("DELETE FROM user_mindmap_permissions WHERE mindmap_id = :mindmapId AND user_id = :userId")
    Mono<Void> deleteByMindmapIdAndUserId(Long mindmapId, Long userId);

    @Query("SELECT * FROM user_mindmap_permissions WHERE user_id = :userId")
    Flux<UserMindmapPermission> findByUserId(Long userId);
}
