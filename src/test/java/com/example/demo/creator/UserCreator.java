package com.example.demo.creator;

import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Component
@Transactional
@RequiredArgsConstructor
public class UserCreator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUserAndModify(String email, String password, Consumer<User> consumer) {
        User user = buildUser(email, password);
        consumer.accept(user);
        return userRepository.save(user);
    }

    public User createUser(String email, String password) {
        return userRepository.save(buildUser(email, password));
    }

    private User buildUser(String email, String password) {
        return User.builder()
                .email(email)
                .phoneNumber("123456789")
                .password(passwordEncoder.encode(password))
                .status(User.UserStatus.ACTIVE)
                .build();
    }
}
