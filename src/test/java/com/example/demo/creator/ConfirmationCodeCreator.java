package com.example.demo.creator;

import com.example.demo.confirmation.code.entity.ConfirmationCode;
import com.example.demo.confirmation.code.repository.ConfirmationCodeRepository;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConfirmationCodeCreator {

    private final ConfirmationCodeRepository confirmationCodeRepository;

    public ConfirmationCode createConfirmationCode(UUID code, User user) {
        return confirmationCodeRepository.save(ConfirmationCode.builder()
            .code(code)
            .user(user)
            .codeType(ConfirmationCode.ConfirmationCodeType.EMAIL_CONFIRMATION)
            .expiresAt(LocalDateTime.now().plusMinutes(15))
            .build());
    }
}
