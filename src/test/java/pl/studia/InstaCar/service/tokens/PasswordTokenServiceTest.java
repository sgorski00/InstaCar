package pl.studia.InstaCar.service.tokens;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import pl.studia.InstaCar.config.exceptions.TokenIllegalArgumentException;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.PasswordResetToken;
import pl.studia.InstaCar.repository.PasswordTokenRepository;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordTokenServiceTest {

    @Mock
    private PasswordTokenRepository passwordTokenRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private PasswordTokenService passwordTokenService;

    private ApplicationUser user;
    private PasswordResetToken token;
    private final String tokenStr = "fakedToken";

    @BeforeEach
    void setUp() {
        user = new ApplicationUser();
        token = new PasswordResetToken();
        token.setUser(user);
        token.setIsUsed(false);
        token.setToken(tokenStr);
        token.setExpiresAt(LocalDateTime.MAX);
    }

    @Test
    void shouldCreateNewToken() {
        ArgumentCaptor<PasswordResetToken> tokenCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);
        passwordTokenService.saveTokenForUser(user);

        verify(passwordTokenRepository, times(1)).save(tokenCaptor.capture());
        PasswordResetToken capturedToken = tokenCaptor.getValue();

        assertEquals(user, capturedToken.getUser());
        assertNotNull(capturedToken.getToken());
        assertFalse(capturedToken.getIsUsed());
        assertFalse(capturedToken.isExpired());
    }

    @Test
    void shouldThrowWhenTokenNotFound() {
        when(passwordTokenRepository.findFirstByTokenOrderByIdDesc(anyString())).thenReturn(Optional.empty());

        TokenIllegalArgumentException exception = assertThrows(TokenIllegalArgumentException.class, () -> passwordTokenService.findLastToken(tokenStr));
        assertEquals(exception.getTokenClass(), PasswordResetToken.class);
        verify(messageSource, times(1)).getMessage(eq("error.token.not.found"), nullable(Object[].class), any(Locale.class));
    }

    @Test
    void shouldReturnCorrectUserAssociatedWithToken() {
        when(passwordTokenRepository.findFirstByTokenOrderByIdDesc(anyString())).thenReturn(Optional.of(token));

        ApplicationUser result = passwordTokenService.getUserForToken(tokenStr);

        assertEquals(result, user);
    }

    @Test
    void shouldThrowIfTokenIsAlreadyUsed() {
        token.setIsUsed(true);
        when(passwordTokenRepository.findFirstByTokenOrderByIdDesc(anyString())).thenReturn(Optional.of(token));

        TokenIllegalArgumentException exception = assertThrows(TokenIllegalArgumentException.class, () ->passwordTokenService.getUserForToken(tokenStr));
        assertEquals(exception.getTokenClass(), PasswordResetToken.class);
        verify(messageSource, times(1)).getMessage(eq("error.token.used"), nullable(Object[].class), any(Locale.class));
    }
}
