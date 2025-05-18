package pl.studia.InstaCar.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.InstaCar.model.dto.UserRegistrationDto;
import pl.studia.InstaCar.service.event.UserRegistrationEvent;
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
    private final MessageSource messageSource;

    @Autowired
    public UserRegistrationService(ApplicationEventPublisher eventPublisher, UserService userService, RoleService roleService, EmailTokenService emailTokenService, MessageSource messageSource) {
        this.eventPublisher = eventPublisher;
        this.userService = userService;
        this.roleService = roleService;
        this.emailTokenService = emailTokenService;
        this.messageSource = messageSource;
    }

    @Transactional
    public void registerUser(UserRegistrationDto userRegisterDto) {
        if(userService.existsByEmail(userRegisterDto.getEmail())) return;
        ApplicationUser user = userRegisterDto.mapToUser(messageSource);
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
            String message = messageSource.getMessage("error.username.exists", null, LocaleContextHolder.getLocale());
            throw new IllegalArgumentException(message);
        } catch (Exception e) {
            log.error(e.getCause());
            String message = messageSource.getMessage("error.register.general", null, LocaleContextHolder.getLocale());
            throw new RuntimeException(message);
        }
    }
}
