package com.example.demo.authentication.controller;

import com.example.demo.IntegrationTest;
import com.example.demo.authentication.dto.RegisterRequest;
import com.example.demo.confirmation.code.entity.ConfirmationCode;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthenticationControllerIT extends IntegrationTest {

    @Test
    void givenValidRegisterRequest_whenRegister_thenCreateUnconfirmedUserAndSendConfirmationCode() throws Exception {
        // given
        String url = "/api/authentication/register";
        String email = "test@gmail.com";
        String phoneNumber = "963852741";
        RegisterRequest request = new RegisterRequest(
            email,
            "pass1234",
            phoneNumber
        );

        // when
        ResultActions resultActions = mockMvc.perform(postJsonRequest(url, request));

        // then
        resultActions.andExpect(status().isOk());

        List<User> users = findAll(User.class);
        assertThat(users).hasSize(1);
        User createdUser = users.get(0);
        assertThat(createdUser.getEmail()).isEqualTo(email);
        assertThat(createdUser.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(createdUser.getStatus()).isEqualTo(User.UserStatus.UNCONFIRMED);

        List<ConfirmationCode> confirmationCodes = findAll(ConfirmationCode.class);
        assertThat(confirmationCodes).hasSize(1);
        assertThat(confirmationCodes.get(0).getUserId()).isEqualTo(createdUser.getId());
        assertThat(confirmationCodes.get(0).getCodeType()).isEqualTo(ConfirmationCode.ConfirmationCodeType.EMAIL_CONFIRMATION);
    }
}
