package pl.studia.InstaCar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.studia.InstaCar.model.authentication.ApplicationUser;

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
}
