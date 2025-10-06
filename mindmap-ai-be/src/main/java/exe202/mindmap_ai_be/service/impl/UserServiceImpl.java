package exe202.mindmap_ai_be.service.impl;

import exe202.mindmap_ai_be.entity.User;
import exe202.mindmap_ai_be.repository.UserRepository;
import exe202.mindmap_ai_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Mono<User> updateUser(Long id, User user) {
        return userRepository.findById(id)
                .flatMap(existing -> {
                    existing.setFullName(user.getFullName());
                    existing.setAvatarUrl(user.getAvatarUrl());
                    existing.setRole(user.getRole());
                    existing.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    return userRepository.save(existing);
                });
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

    @Override
    public Mono<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
