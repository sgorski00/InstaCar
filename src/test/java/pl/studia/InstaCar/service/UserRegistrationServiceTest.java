package pl.studia.InstaCar.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pl.studia.InstaCar.model.dto.UserRegistrationDto;
import pl.studia.InstaCar.service.event.UserRegistrationEvent;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.EmailActivationToken;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.service.tokens.EmailTokenService;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRegistrationServiceTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private EmailTokenService emailTokenService;

    @InjectMocks
    private UserRegistrationService userRegistrationService;

    UserRegistrationDto user;
    Role userRole;

    @BeforeEach
    public void setUp() {
        user = new UserRegistrationDto();
        user.setUsername("username");
        user.setPassword("password");
        user.setConfirmPassword("password");
        user.setEmail("email");
        userRole = new Role();
    }

    @Test
    void shouldRegisterUser() {
        when(roleService.findByName(anyString())).thenReturn(userRole);
        when(emailTokenService.saveTokenForUser(any(ApplicationUser.class))).thenReturn(new EmailActivationToken());

        userRegistrationService.registerUser(user);

        verify(userService, times(1)).save(any(ApplicationUser.class));
        verify(emailTokenService, times(1)).saveTokenForUser(any(ApplicationUser.class));
        verify(eventPublisher, times(1)).publishEvent(any(UserRegistrationEvent.class));
    }

    @Test
    void shouldNotRegisterWhenRoleNotFound() {
        when(roleService.findByName(anyString())).thenThrow(
                new NoSuchElementException("Nie odnaleziono roli: user")
        );

        assertThrows(
                NoSuchElementException.class,
                () -> userRegistrationService.registerUser(user)
        );

        verify(userService, never()).save(any(ApplicationUser.class));
        verify(eventPublisher, never()).publishEvent(any(UserRegistrationEvent.class));
    }

    @Test
    void shouldNotRegisterWhenUserIsNull() {
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> userRegistrationService.registerUser(null)
        );

        assertTrue(thrown.getMessage().contains("is null"));
        verify(userService, never()).save(any(ApplicationUser.class));
        verify(eventPublisher, never()).publishEvent(any(UserRegistrationEvent.class));
    }


}
