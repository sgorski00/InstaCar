package pl.studia.InstaCar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.EmailToken;

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
        String fakeEncryptedPassword = "2a$faked";
        applicationUser.setPassword(fakeEncryptedPassword);

        applicationUser.encryptPassword();
        assertNotNull(applicationUser.getPassword());
        assertEquals(fakeEncryptedPassword, applicationUser.getPassword());
    }

    @Test
    void shouldReturnTrueIfVerifiedWithOneToken() {
        EmailToken emailToken = new EmailToken();
        emailToken.setIsVerified(true);
        applicationUser.setEmailTokens(List.of(emailToken));

        boolean result = applicationUser.isEnabled();

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueIfVerifiedWithMultipleTokens() {
        EmailToken emailToken = new EmailToken();
        emailToken.setIsVerified(true);
        EmailToken emailToken2 = new EmailToken();
        emailToken2.setIsVerified(true);
        applicationUser.setEmailTokens(List.of(emailToken, emailToken2));

        boolean result = applicationUser.isEnabled();

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfVerifiedWithMultipleTokens() {
        EmailToken emailToken = new EmailToken();
        emailToken.setIsVerified(false);
        EmailToken emailToken2 = new EmailToken();
        emailToken2.setIsVerified(false);
        applicationUser.setEmailTokens(List.of(emailToken, emailToken2));

        boolean result = applicationUser.isEnabled();

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseIfVerifiedWithOneToken() {
        EmailToken emailToken = new EmailToken();
        emailToken.setIsVerified(false);
        applicationUser.setEmailTokens(List.of(emailToken));

        boolean result = applicationUser.isEnabled();

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueIfVerifiedWithMixedTokens() {
        EmailToken emailToken = new EmailToken();
        emailToken.setIsVerified(false);
        EmailToken emailToken2 = new EmailToken();
        emailToken.setIsVerified(true);
        applicationUser.setEmailTokens(List.of(emailToken, emailToken2));

        boolean result = applicationUser.isEnabled();

        assertTrue(result);
    }
}
