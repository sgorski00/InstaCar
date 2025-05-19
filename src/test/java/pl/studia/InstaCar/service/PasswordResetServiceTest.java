package pl.studia.InstaCar.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
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

    @Mock
    private MessageSource messageSource;

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
        String token = "fakedToken";
        ApplicationUser user = new ApplicationUser();
        PasswordResetDto resetDto = new PasswordResetDto(token);
        resetDto.setPassword("password");
        resetDto.setConfirmPassword("password");
        resetDto.setToken(token);

        passwordResetService.changePassword(user, resetDto);

        verify(userService, times(1)).save(user);
        verify(passwordTokenService, times(1)).setTokenActivated(token);
    }
}