package exe202.mindmap_ai_be.service;

import exe202.mindmap_ai_be.entity.User;
import exe202.mindmap_ai_be.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface UserService {
    Mono<User> getUserById(Long id);
    Flux<User> getAllUsers();
    Mono<User> createUser(User user);
    Mono<User> updateUser(Long id, User user);
    Mono<Void> deleteUser(Long id);
    Mono<Void> getUserByEmail(String email);

}
