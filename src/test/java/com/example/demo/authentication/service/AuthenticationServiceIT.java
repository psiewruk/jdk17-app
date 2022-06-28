package com.example.demo.authentication.service;


import com.example.demo.IntegrationTest;
import com.example.demo.authentication.dto.LoginRequest;
import com.example.demo.authentication.dto.LoginResponse;
import com.example.demo.creator.ConfirmationCodeCreator;
import com.example.demo.creator.UserCreator;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class AuthenticationServiceIT extends IntegrationTest {

    @Autowired
    private AuthenticationService underTest;
    @Autowired
    private UserCreator userCreator;
    @Autowired
    private ConfirmationCodeCreator confirmationCodeCreator;

    @Test
    void givenRequestWithCorrectPassword_whenAuthenticate_thenReturnToken() {
        // given
        String email = "test@test.com";
        String password = "pass123";
        userCreator.createUser(email, password);

        LoginRequest request = LoginRequest.builder()
                .username(email)
                .password(password)
                .build();

        // when
        LoginResponse response = underTest.authenticate(request);

        // then
        assertThat(response.jwtToken()).isNotEmpty();
    }

    @Test
    void givenRequestWithIncorrectPassword_whenAuthenticate_thenThrow() {
        // given
        String email = "test@test.com";
        String password = "pass123";
        userCreator.createUser(email, password);

        LoginRequest request = LoginRequest.builder()
                .username(email)
                .password("123pass")
                .build();

        // when
        Throwable thrown = catchThrowable(() -> underTest.authenticate(request));

        // then
        assertThat(thrown).isExactlyInstanceOf(BadCredentialsException.class);
    }

    @Test
    void givenRequestForDisabledUser_whenAuthenticate_thenThrow() {
        // given
        String email = "test@test.com";
        String password = "pass123";
        userCreator.createUserAndModify(email, password, user -> user.setStatus(User.UserStatus.BLOCKED));

        LoginRequest request = LoginRequest.builder()
                .username(email)
                .password("123pass")
                .build();

        // when
        Throwable thrown = catchThrowable(() -> underTest.authenticate(request));

        // then
        assertThat(thrown).isExactlyInstanceOf(DisabledException.class);
    }

    @Test
    void givenEmailConfirmationRequest_whenConfirmEmail_thenActivateUser() {
        // given
        UUID code = UUID.randomUUID();
        String email = "test@gmail.com";

        User user = userCreator.createUserAndModify(email, "pass123", u -> u.setStatus(User.UserStatus.UNCONFIRMED));
        confirmationCodeCreator.createConfirmationCode(code, user);

        // when
        underTest.confirmEmail(code);

        // then
        User dbUser = findById(User.class, user.getId());
        assertThat(dbUser.getStatus()).isEqualTo(User.UserStatus.ACTIVE);
        assertThat(dbUser.getId()).isEqualTo(user.getId());
    }
}