package exe202.mindmap_ai_be.dtos;

import exe202.mindmap_ai_be.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long userId;
    private String fullName;
    private String avatarUrl;
    private String role;
}
