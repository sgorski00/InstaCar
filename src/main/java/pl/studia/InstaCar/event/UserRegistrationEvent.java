package pl.studia.InstaCar.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.EmailToken;

@Getter
public class UserRegistrationEvent extends ApplicationEvent {
    private final ApplicationUser user;
    private final EmailToken emailToken;

    public UserRegistrationEvent(Object source, ApplicationUser user, EmailToken emailToken) {
        super(source);
        this.user = user;
        this.emailToken = emailToken;
    }
}
