package pl.studia.InstaCar.service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.EmailActivationToken;

@Getter
public class UserRegistrationEvent extends ApplicationEvent {
    private final ApplicationUser user;
    private final EmailActivationToken emailToken;

    public UserRegistrationEvent(Object source, ApplicationUser user, EmailActivationToken emailToken) {
        super(source);
        this.user = user;
        this.emailToken = emailToken;
    }
}
