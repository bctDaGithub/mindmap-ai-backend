package exe202.mindmap_ai_be.service.impl;

import exe202.mindmap_ai_be.entity.User;
import exe202.mindmap_ai_be.repository.UserRepository;
import exe202.mindmap_ai_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> getUserById(Long id) {
        return null;
    }

    @Override
    public Flux<User> getAllUsers() {
        return null;
    }

    @Override
    public Mono<User> createUser(User user) {
        return null;
    }

    @Override
    public Mono<User> updateUser(Long id, User user) {
        return null;
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        return null;
    }

    @Override
    public Mono<Void> getUserByEmail(String email) {
        return null;
    }
}
