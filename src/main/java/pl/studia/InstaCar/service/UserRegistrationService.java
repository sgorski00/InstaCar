package pl.studia.InstaCar.service;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.RollbackException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.InstaCar.config.exceptions.EntityValidationException;
import pl.studia.InstaCar.model.dto.UserRegistrationDto;
import pl.studia.InstaCar.service.event.UserRegistrationEvent;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.EmailActivationToken;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.service.tokens.EmailTokenService;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Service
@Log4j2
public class UserRegistrationService {
    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;
    private final RoleService roleService;
    private final EmailTokenService emailTokenService;

    @Autowired
    public UserRegistrationService(ApplicationEventPublisher eventPublisher, UserService userService, RoleService roleService, EmailTokenService emailTokenService) {
        this.eventPublisher = eventPublisher;
        this.userService = userService;
        this.roleService = roleService;
        this.emailTokenService = emailTokenService;
    }

    @Transactional
    public void registerUser(UserRegistrationDto userRegisterDto) {
        if(userService.existsByEmail(userRegisterDto.getEmail())) return;
        ApplicationUser user = userRegisterDto.mapToUser();
        try {
            Role role = roleService.findByName("user");
            user.setRole(role);
            userService.save(user);
            EmailActivationToken token = emailTokenService.saveTokenForUser(user);
            eventPublisher.publishEvent(new UserRegistrationEvent(this, user, token));
        } catch (NoSuchElementException | IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new NoSuchElementException(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new EntityValidationException("Podana nazwa użytkownika jest zajęta", "/register");
        } catch (Exception e) {
            log.error(e.getCause());
            throw new RuntimeException("An error occurred while registering user");
        }
    }
}
