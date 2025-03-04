package pl.studia.InstaCar.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pl.studia.InstaCar.event.UserRegistrationEvent;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.EmailToken;
import pl.studia.InstaCar.model.authentication.Role;

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

    ApplicationUser user;
    Role userRole;

    @BeforeEach
    public void setUp() {
        user = new ApplicationUser();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email");
        userRole = new Role();
    }

    @Test
    void shouldRegisterUser() {
        when(roleService.findByName(anyString())).thenReturn(userRole);
        when(emailTokenService.saveTokenForUser(any(ApplicationUser.class))).thenReturn(new EmailToken());

        userRegistrationService.registerUser(user);

        verify(userService, times(1)).save(any(ApplicationUser.class));
        assertEquals(userRole, user.getRole());
        verify(eventPublisher, times(1)).publishEvent(any(UserRegistrationEvent.class));
    }

    @Test
    void shouldNotRegisterWhenRoleNotFound() {
        when(roleService.findByName(anyString())).thenThrow(new NoSuchElementException());

        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class,
                () -> userRegistrationService.registerUser(user)
        );

        assertTrue(thrown.getMessage().contains("not found"));
        verify(userService, never()).save(any(ApplicationUser.class));
        verify(eventPublisher, never()).publishEvent(any(UserRegistrationEvent.class));
    }

    @Test
    void shouldNotRegisterWhenUserIsNull() {
        when(roleService.findByName(anyString())).thenReturn(userRole);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> userRegistrationService.registerUser(null)
        );

        assertTrue(thrown.getMessage().contains("error occurred"));
        verify(userService, never()).save(any(ApplicationUser.class));
        verify(eventPublisher, never()).publishEvent(any(UserRegistrationEvent.class));
    }
}
