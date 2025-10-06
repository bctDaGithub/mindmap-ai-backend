package exe202.mindmap_ai_be.entity;

import exe202.mindmap_ai_be.entity.enums.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;


@Table(name = "users")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column("userid")
    private Long userId;

    @Column("email")
    private String email;

    @Column("passwordhash")
    private String passwordHash;

    @Column("fullname")
    private String fullName;

    @Column("avatarurl")
    private String avatarUrl;

    @Column("role")
    private String role;

    @Column("createdat")
    private Timestamp createdAt;

    @Column("updatedat")
    private Timestamp updatedAt;
}
