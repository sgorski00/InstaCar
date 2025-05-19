package pl.studia.InstaCar.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.repository.RoleRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private RoleService roleService;

    @Test
    void shouldReturnTrueIfListIsEmpty() {
        when(roleRepository.count()).thenReturn(0L);

        assertTrue(roleService.isEmpty());
    }

    @Test
    void shouldReturnFalseIfListIsEmpty() {
        when(roleRepository.count()).thenReturn(1L);

        assertFalse(roleService.isEmpty());
    }

    @Test
    void shouldReturnRoleByName() {
        when(roleRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(new Role()));

        Role role = roleService.findByName("role");

        assertNotNull(role);
    }

    @Test
    void shouldThrowIfRoleNotFound() {
        when(roleRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class,
                () -> roleService.findByName("not exist")
        );

        assertTrue(thrown.getMessage().contains("not exist"));
    }

    @Test
    void shouldSaveNewRole(){
        roleService.save(new Role());

        verify(roleRepository, times(1)).save(any(Role.class));
    }
}
