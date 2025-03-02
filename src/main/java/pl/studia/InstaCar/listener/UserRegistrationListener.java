package pl.studia.InstaCar.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.studia.InstaCar.event.UserRegistrationEvent;

@Component
@Log4j2
public class UserRegistrationListener {

    @Async
    @EventListener
    public void onUserRegistration(UserRegistrationEvent event) {
        log.info("New user has registered! Email sending...");
        //TODO(handle emails sending)
    }
}
