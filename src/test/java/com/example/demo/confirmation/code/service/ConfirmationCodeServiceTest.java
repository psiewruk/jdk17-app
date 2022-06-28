package com.example.demo.confirmation.code.service;

import com.example.demo.UnitTest;
import com.example.demo.config.properties.ConfirmationCodeProperties;
import com.example.demo.confirmation.code.entity.ConfirmationCode;
import com.example.demo.confirmation.code.exception.InvalidConfirmationCodeException;
import com.example.demo.confirmation.code.repository.ConfirmationCodeRepository;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static com.example.demo.confirmation.code.entity.ConfirmationCode.ConfirmationCodeType.EMAIL_CONFIRMATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConfirmationCodeServiceTest extends UnitTest {

    @InjectMocks
    private ConfirmationCodeService underTest;

    @Mock
    private ConfirmationCodeRepository confirmationCodeRepository;
    @Mock
    private ConfirmationCodeProperties confirmationCodeProperties;
    @Mock
    private Clock clock;
    @Captor
    private ArgumentCaptor<ConfirmationCode> codeCaptor;

    @Test
    void givenUser_whenCreateConfirmationCode_thenSaveAndReturnCode() {
        // given
        Long userId = 321L;
        User user = User.builder()
            .id(userId)
            .email("test@gmail.com")
            .build();

        when(confirmationCodeProperties.getEmailValidityTime()).thenReturn(15);
        when(clock.instant()).thenReturn(Instant.now());
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
        doAnswer(invocation -> invocation.getArgument(0)).when(confirmationCodeRepository).save(any());

        // when
        UUID code = underTest.generateEmailConfirmationCode(user);

        // then
        verify(confirmationCodeRepository).save(codeCaptor.capture());
        ConfirmationCode captured = codeCaptor.getValue();
        assertThat(captured.getCode()).isEqualTo(code);
        assertThat(captured.getExpiresAt()).isEqualToIgnoringSeconds(LocalDateTime.now(clock).plusMinutes(15));
        assertThat(captured.getUser()).isEqualTo(user);
    }

    @Test
    void givenUserAndExistingConfirmationCode_whenConfirmEmailCode_thenSetUserAsActive() {
        // given
        UUID code = UUID.randomUUID();
        User user = User.builder()
            .id(123L)
            .email("test@gmail.com")
            .build();
        ConfirmationCode confirmationCode = ConfirmationCode.builder()
            .code(code)
            .user(user)
            .build();

        when(confirmationCodeRepository.findActiveByCodeAndType(code, EMAIL_CONFIRMATION))
            .thenReturn(Optional.of(confirmationCode));

        // when
        underTest.confirmEmailCode(code);

        // then
        verify(confirmationCodeRepository).delete(confirmationCode);
        assertThat(user.getStatus()).isEqualTo(User.UserStatus.ACTIVE);
    }

    @Test
    void givenUserAndNotExistingConfirmationCode_whenConfirmEmailCode_thenThrowException() {
        // given
        UUID code = UUID.randomUUID();
        User user = User.builder()
            .id(123L)
            .email("test@gmail.com")
            .build();

        when(confirmationCodeRepository.findActiveByCodeAndType(code, EMAIL_CONFIRMATION))
            .thenReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> underTest.confirmEmailCode(code));

        // then
        verify(confirmationCodeRepository, times(0)).delete(any(ConfirmationCode.class));
        assertThat(thrown).isExactlyInstanceOf(InvalidConfirmationCodeException.class);
    }

}
