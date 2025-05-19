package pl.studia.InstaCar.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.AuthProvider;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.model.authentication.tokens.EmailActivationToken;
import pl.studia.InstaCar.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private UserService userService;

    private ApplicationUser user;

    @BeforeEach
    void setUp() {
        EmailActivationToken token = new EmailActivationToken();
        token.setIsUsed(true);
        Role role = new Role();
        role.setName("user");
        user = new ApplicationUser();
        user.setUsername("user");
        user.setProvider(AuthProvider.LOCAL);
        user.setEmailTokens(List.of(token));
        user.setRole(role);
    }

    @Test
    void shouldLoadUser() {
        when(userRepository.findByUsernameIgnoreCase("user")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("user");

        assertNotNull(result);
        assertEquals("user", result.getUsername());
    }

    @Test
    void shouldNotLoadUserIfNotFound() {
        when(userRepository.findByUsernameIgnoreCase("user")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("user"));
    }

    @Test
    void shouldNotLoadUserIfNotEnabled() {
        user.setEmailTokens(new ArrayList<>());

        when(userRepository.findByUsernameIgnoreCase("user")).thenReturn(Optional.of(user));

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("user"));
    }

    @Test
    void shouldNotLoadUserIfNotLocalAccount() {
        user.setProvider(AuthProvider.GOOGLE);
        when(userRepository.findByUsernameIgnoreCase("user")).thenReturn(Optional.of(user));

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("user"));
    }
}
