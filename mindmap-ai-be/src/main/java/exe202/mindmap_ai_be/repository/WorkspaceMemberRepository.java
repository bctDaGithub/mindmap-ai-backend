package exe202.mindmap_ai_be.repository;

import exe202.mindmap_ai_be.entity.WorkspaceMember;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WorkspaceMemberRepository extends ReactiveCrudRepository<WorkspaceMember,Long> {

    @Query("SELECT * FROM workspace_members WHERE workspace_id = :workspaceId")
    Flux<WorkspaceMember> findByWorkspaceId(Long workspaceId);

    @Query("SELECT * FROM workspace_members WHERE workspace_id = :workspaceId AND user_id = :userId")
    Mono<WorkspaceMember> findByWorkspaceIdAndUserId(Long workspaceId, Long userId);

    @Query("DELETE FROM workspace_members WHERE workspace_id = :workspaceId AND user_id = :userId")
    Mono<Void> deleteByWorkspaceIdAndUserId(Long workspaceId, Long userId);

    @Query("SELECT * FROM workspace_members WHERE user_id = :userId")
    Flux<WorkspaceMember> findByUserId(Long userId);
}
