package com.example.demo.user.service;

import com.example.demo.authentication.dto.RegisterRequest;
import com.example.demo.user.entity.User;
import com.example.demo.user.exception.DuplicatedEmailException;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    @Lazy
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s not found", username)));
    }

    public User createUser(RegisterRequest request) {
        assertEmailNotTaken(request.email());

        return userRepository.save(User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .status(User.UserStatus.UNCONFIRMED)
                .build());
    }

    private void assertEmailNotTaken(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicatedEmailException(email);
        }
    }
}
