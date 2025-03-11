package pl.studia.InstaCar.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.InstaCar.event.UserRegistrationEvent;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.EmailActivationToken;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.service.tokens.EmailTokenService;

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
    public void registerUser(ApplicationUser user) {
        try {
            Role role = roleService.findByName("user");
            user.setRole(role);
            userService.save(user);
            EmailActivationToken token = emailTokenService.saveTokenForUser(user);
            eventPublisher.publishEvent(new UserRegistrationEvent(this, user, token));
        } catch (NoSuchElementException | IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            log.error(e.getCause());
            throw new RuntimeException("An error occurred while registering user");
        }
    }
}
