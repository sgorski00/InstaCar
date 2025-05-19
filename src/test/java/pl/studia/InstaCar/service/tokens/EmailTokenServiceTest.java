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
import pl.studia.InstaCar.model.authentication.AuthProvider;
import pl.studia.InstaCar.model.authentication.tokens.EmailActivationToken;
import pl.studia.InstaCar.repository.EmailTokenRepository;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailTokenServiceTest {

    @Mock
    private EmailTokenRepository emailTokenRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private EmailTokenService emailTokenService;

    private EmailActivationToken token;
    private ApplicationUser user;

    @BeforeEach
    void setUp() {
        user = new ApplicationUser();
        token = new EmailActivationToken();
        token.setUser(user);
        token.setIsUsed(false);
        token.setToken("fakedToken");
        token.setExpiresAt(LocalDateTime.MAX);
    }

    @Test
    void shouldSetTokenActivated() {
        when(emailTokenRepository.findFirstByTokenOrderByIdDesc(anyString())).thenReturn(Optional.of(token));

        emailTokenService.setTokenActivated("fakedToken");

        assertTrue(token.getIsUsed());
        verify(emailTokenRepository, times(1)).save(any(EmailActivationToken.class));

    }

    @Test
    void shouldNotSetTokenActivatedIfTokenNotFound() {
        when(emailTokenRepository.findFirstByTokenOrderByIdDesc(anyString())).thenReturn(Optional.empty());

        assertThrows(TokenIllegalArgumentException.class, () -> emailTokenService.setTokenActivated("fakedToken"));
        assertFalse(token.getIsUsed());
        verify(messageSource, times(1)).getMessage(eq("error.token.not.found"), nullable(Object[].class), any(Locale.class));
        verify(emailTokenRepository, times(0)).save(any(EmailActivationToken.class));
    }

    @Test
    void shouldNotSetTokenActivatedIfTokenIsExpired() {
        token.setExpiresAt(LocalDateTime.MIN);
        when(emailTokenRepository.findFirstByTokenOrderByIdDesc(anyString())).thenReturn(Optional.of(token));

        assertThrows(IllegalArgumentException.class, () ->emailTokenService.setTokenActivated("fakedToken"));
        assertFalse(token.getIsUsed());
        verify(messageSource, times(1)).getMessage(eq("error.token.expired"), nullable(Object[].class), any(Locale.class));
        verify(emailTokenRepository, times(0)).save(any(EmailActivationToken.class));
    }

    @Test
    void shouldNotSetTokenActivatedIfTokenIsAlreadyUsed() {
        token.setIsUsed(true);
        when(emailTokenRepository.findFirstByTokenOrderByIdDesc(anyString())).thenReturn(Optional.of(token));

        assertThrows(IllegalArgumentException.class, () ->emailTokenService.setTokenActivated("fakedToken"));
        assertTrue(token.getIsUsed());
        verify(messageSource, times(1)).getMessage(eq("error.token.used"), nullable(Object[].class), any(Locale.class));
        verify(emailTokenRepository, times(0)).save(any(EmailActivationToken.class));
    }

    @Test
    void shouldCreateNewToken() {
        ArgumentCaptor<EmailActivationToken> tokenCaptor = ArgumentCaptor.forClass(EmailActivationToken.class);
        emailTokenService.saveTokenForUser(user);

        verify(emailTokenRepository, times(1)).save(tokenCaptor.capture());
        EmailActivationToken capturedToken = tokenCaptor.getValue();

        assertEquals(user, capturedToken.getUser());
        assertNotNull(capturedToken.getToken());
        assertFalse(capturedToken.getIsUsed());
        assertFalse(capturedToken.isExpired());
    }

    @Test
    void shouldNotCreateNewTokenIfNullUser() {
        assertThrows(IllegalArgumentException.class, () -> emailTokenService.saveTokenForUser(null));

        verify(emailTokenRepository, times(0)).save(any(EmailActivationToken.class));
    }

    @Test
    void shouldNotCreateNewTokenIfNotLocalAccount() {
        user.setProvider(AuthProvider.GOOGLE);
        assertThrows(IllegalArgumentException.class, () -> emailTokenService.saveTokenForUser(user));

        verify(emailTokenRepository, times(0)).save(any(EmailActivationToken.class));
    }

    @Test
    void shouldThrowWhenTokenNotFound() {
        when(emailTokenRepository.findFirstByTokenOrderByIdDesc(anyString())).thenReturn(Optional.empty());

        TokenIllegalArgumentException exception = assertThrows(TokenIllegalArgumentException.class, () -> emailTokenService.findLastToken("fakedToken"));
        assertEquals(exception.getTokenClass(), EmailActivationToken.class);
        verify(messageSource, times(1)).getMessage(eq("error.token.not.found"), nullable(Object[].class), any(Locale.class));
    }
}