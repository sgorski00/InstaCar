package pl.studia.InstaCar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.EmailActivationToken;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationUserTest {

    private PasswordEncoder passwordEncoder;
    private ApplicationUser applicationUser;

    @BeforeEach
    public void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        ApplicationUser.setPasswordEncoder(passwordEncoder);
        applicationUser = new ApplicationUser();
    }

    @Test
    void shouldEncryptPassword() {
        String rawPassword = "password";
        applicationUser.setPassword(rawPassword);
        applicationUser.encryptPassword();

        assertNotNull(applicationUser.getPassword());
        assertTrue(passwordEncoder.matches(rawPassword, applicationUser.getPassword()));
    }

    @Test
    void shouldNotEncryptPasswordIfNull() {
        applicationUser.encryptPassword();

        assertNull(applicationUser.getPassword());
    }

    @Test
    void shouldNotEncryptPasswordIfAlreadyEncrypted() {
        String fakeEncryptedPassword = "$2a$faked";
        applicationUser.setPassword(fakeEncryptedPassword);

        applicationUser.encryptPassword();
        assertNotNull(applicationUser.getPassword());
        assertEquals(fakeEncryptedPassword, applicationUser.getPassword());
    }

    @Test
    void shouldReturnTrueIfVerifiedWithOneToken() {
        EmailActivationToken emailToken = new EmailActivationToken();
        emailToken.setIsUsed(true);
        applicationUser.setEmailTokens(List.of(emailToken));

        boolean result = applicationUser.isEnabled();

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueIfVerifiedWithMultipleTokens() {
        EmailActivationToken emailToken = new EmailActivationToken();
        emailToken.setIsUsed(true);
        EmailActivationToken emailToken2 = new EmailActivationToken();
        emailToken2.setIsUsed(true);
        applicationUser.setEmailTokens(List.of(emailToken, emailToken2));

        boolean result = applicationUser.isEnabled();

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfVerifiedWithMultipleTokens() {
        EmailActivationToken emailToken = new EmailActivationToken();
        emailToken.setIsUsed(false);
        EmailActivationToken emailToken2 = new EmailActivationToken();
        emailToken2.setIsUsed(false);
        applicationUser.setEmailTokens(List.of(emailToken, emailToken2));

        boolean result = applicationUser.isEnabled();

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseIfVerifiedWithOneToken() {
        EmailActivationToken emailToken = new EmailActivationToken();
        emailToken.setIsUsed(false);
        applicationUser.setEmailTokens(List.of(emailToken));

        boolean result = applicationUser.isEnabled();

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueIfVerifiedWithMixedTokens() {
        EmailActivationToken emailToken = new EmailActivationToken();
        emailToken.setIsUsed(false);
        EmailActivationToken emailToken2 = new EmailActivationToken();
        emailToken.setIsUsed(true);
        applicationUser.setEmailTokens(List.of(emailToken, emailToken2));

        boolean result = applicationUser.isEnabled();

        assertTrue(result);
    }
}
