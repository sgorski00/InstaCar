package pl.studia.InstaCar.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.PasswordResetToken;
import pl.studia.InstaCar.model.dto.PasswordResetDto;
import pl.studia.InstaCar.service.event.PasswordResetEvent;
import pl.studia.InstaCar.service.tokens.PasswordTokenService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private PasswordTokenService passwordTokenService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PasswordResetService passwordResetService;

    @Test
    void shouldGenerateToken() {
        ApplicationUser user = new ApplicationUser();
        PasswordResetToken token = new PasswordResetToken();
        when(passwordTokenService.saveTokenForUser(user)).thenReturn(token);

        passwordResetService.generateToken(user);

        verify(passwordTokenService, times(1)).saveTokenForUser(user);
        verify(eventPublisher, times(1)).publishEvent(any(PasswordResetEvent.class));
    }

    @Test
    void shouldThrowIfGenerateTokenFails() {
        ApplicationUser user = new ApplicationUser();
        when(passwordTokenService.saveTokenForUser(user)).thenThrow();

        assertThrows(RuntimeException.class, () -> passwordResetService.generateToken(user));
    }

    @Test
    void shouldChangePassword() {
        ApplicationUser user = new ApplicationUser();
        PasswordResetDto resetDto = new PasswordResetDto();
        resetDto.setPassword("password");
        resetDto.setRepeatPassword("password");
        String token = "fakedToken";

        passwordResetService.changePassword(user, resetDto, token);

        verify(userService, times(1)).save(user);
        verify(passwordTokenService, times(1)).setTokenActivated(token);
    }
}