package exe202.mindmap_ai_be.controller;

import exe202.mindmap_ai_be.dto.request.LoginRequest;
import exe202.mindmap_ai_be.dto.request.RegisterRequest;
import exe202.mindmap_ai_be.dtos.LoginResponse;
import exe202.mindmap_ai_be.dtos.TokenRequest;
import exe202.mindmap_ai_be.entity.User;
import exe202.mindmap_ai_be.entity.enums.Role;
import exe202.mindmap_ai_be.jwt.JwtUtil;
import exe202.mindmap_ai_be.service.GoogleAuthService;
import exe202.mindmap_ai_be.service.impl.GoogleAuthServiceImpl;
import exe202.mindmap_ai_be.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final GoogleAuthService googleAuthService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(GoogleAuthServiceImpl googleAuthService, UserService userService, JwtUtil jwtUtil) {
        this.googleAuthService = googleAuthService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/register")
    public Mono<LoginResponse> register(@RequestBody RegisterRequest request) {
        return userService.getUserByEmail(request.getEmail())   // Mono<User>
                .hasElement()                                       // Mono<Boolean>
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.<User>error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Tài khoản đã tồn tại"));
                    }
                    return userService.createUser(new User(
                            null, request.getEmail(), request.getPassword(),
                            request.getFullName(), null, Role.USER.name(),
                            new Timestamp(System.currentTimeMillis()),
                            new Timestamp(System.currentTimeMillis())
                    ));
                })
                .map(user -> {
                    String jwt = jwtUtil.generateToken(user.getEmail(), user.getRole());
                    return new LoginResponse(jwt, user.getUserId(), user.getFullName(), user.getAvatarUrl(), user.getRole());
                });
    }


    // ✅ 2. Login
    @PostMapping("/login")
    public Mono<LoginResponse> login(@RequestBody LoginRequest request) {
        return userService.getUserByEmail(request.getEmail())
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid email or password")))
                .flatMap(user -> {
                    if (request.getPassword().equals(user.getPasswordHash())) {
                        String jwt = jwtUtil.generateToken(user.getEmail(), user.getRole());
                        return Mono.just(new LoginResponse(jwt, user.getUserId(), user.getFullName(), user.getAvatarUrl(), user.getRole()));
                    } else {
                        return Mono.error(new RuntimeException("Invalid email or password"));
                    }
                });
    }
    @PostMapping("/google")
    public Mono<LoginResponse> loginWithGoogle(@RequestBody TokenRequest request) {
        return googleAuthService.verify(request.getIdToken())
                .flatMap(payload -> {
                    String email = payload.getEmail();
                    String name = (String) payload.get("name");
                    String picture = (String) payload.get("picture");

                    return userService.getUserByEmail(email)
                            .switchIfEmpty(
                                    userService.createUser(new User(
                                            null, // id
                                            email,
                                            null, // password (null vì login Google)
                                            name,
                                            picture,
                                            Role.USER.name(),
                                            new Timestamp(System.currentTimeMillis()),
                                            new Timestamp(System.currentTimeMillis())
                                    ))
                            )
                            .map(user -> {
                                String jwt = jwtUtil.generateToken(email, user.getRole());
                                return new LoginResponse(jwt, user.getUserId(), user.getFullName(), user.getAvatarUrl(), user.getRole());
                            });
                });
    }
}
