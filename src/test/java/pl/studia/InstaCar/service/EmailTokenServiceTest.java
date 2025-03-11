package pl.studia.InstaCar.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.EmailActivationToken;
import pl.studia.InstaCar.repository.EmailTokenRepository;
import pl.studia.InstaCar.service.tokens.EmailTokenService;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailTokenServiceTest {

    @Mock
    private EmailTokenRepository emailTokenRepository;

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

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () ->emailTokenService.setTokenActivated("fakedToken"));

        assertTrue(thrown.getMessage().contains("token nie istnieje"));
        assertFalse(token.getIsUsed());
        verify(emailTokenRepository, times(0)).save(any(EmailActivationToken.class));
    }

    @Test
    void shouldCreateNewToken() {
        EmailActivationToken emailToken = emailTokenService.saveTokenForUser(user);

        verify(emailTokenRepository, times(1)).save(any(EmailActivationToken.class));
    }

    @Test
    void shouldNotCreateNewTokenIfNullUser() {
        assertThrows(IllegalArgumentException.class, () -> emailTokenService.saveTokenForUser(null));

        verify(emailTokenRepository, times(0)).save(any(EmailActivationToken.class));
    }
}
