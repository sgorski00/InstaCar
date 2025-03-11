package pl.studia.InstaCar.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.PasswordResetToken;

@Getter
public class PasswordResetEvent extends ApplicationEvent {
    private final ApplicationUser user;
    private final PasswordResetToken resetToken;

    public PasswordResetEvent(Object source, ApplicationUser user, PasswordResetToken resetToken) {
        super(source);
        this.user = user;
        this.resetToken = resetToken;
    }
}
