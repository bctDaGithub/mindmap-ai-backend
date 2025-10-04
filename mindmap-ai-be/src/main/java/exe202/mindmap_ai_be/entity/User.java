package exe202.mindmap_ai_be.entity;

import exe202.mindmap_ai_be.entity.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;


@Table(name = "users")
@Data
@RequiredArgsConstructor
public class User {

    @Id
    private Long userId;

    private String email;
    private String passwordHash;
    private String fullName;
    private String avatarUrl;
    private Role role;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
