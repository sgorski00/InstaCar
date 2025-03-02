package pl.studia.InstaCar.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.studia.InstaCar.model.authentication.ApplicationUser;

@Getter
public class UserRegistrationEvent extends ApplicationEvent {
    private final ApplicationUser user;

    public UserRegistrationEvent(Object source, ApplicationUser user) {
        super(source);
        this.user = user;
    }
}
