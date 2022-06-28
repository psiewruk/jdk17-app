package com.example.demo.confirmation.code.service;

import com.example.demo.config.properties.ConfirmationCodeProperties;
import com.example.demo.confirmation.code.entity.ConfirmationCode;
import com.example.demo.confirmation.code.exception.InvalidConfirmationCodeException;
import com.example.demo.confirmation.code.repository.ConfirmationCodeRepository;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.demo.confirmation.code.entity.ConfirmationCode.ConfirmationCodeType.EMAIL_CONFIRMATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationCodeService {

    private final Clock clock;
    private final ConfirmationCodeRepository confirmationCodeRepository;
    private final ConfirmationCodeProperties confirmationCodeProperties;

    public UUID generateEmailConfirmationCode(User user) {
        ConfirmationCode confirmationCode = confirmationCodeRepository.save(ConfirmationCode.builder()
            .user(user)
            .code(UUID.randomUUID())
            .expiresAt(LocalDateTime.now(clock).plusMinutes(confirmationCodeProperties.getEmailValidityTime()))
            .codeType(EMAIL_CONFIRMATION)
            .build());

        return confirmationCode.getCode();
    }

    public void confirmEmailCode(UUID confirmationCode) {
        confirmationCodeRepository.findActiveByCodeAndType(confirmationCode, EMAIL_CONFIRMATION)
            .ifPresentOrElse(code -> {
                User user = code.getUser();
                user.setStatus(User.UserStatus.ACTIVE);
                confirmationCodeRepository.delete(code);
                log.info("User {} confirmed and activated", user.getEmail());
            }, () -> {
                throw new InvalidConfirmationCodeException();
            });
    }

    @Scheduled(cron = "0 * * * * ?")
    public void deleteExpiredCodes() {
        confirmationCodeRepository.deleteExpired();
    }
}
