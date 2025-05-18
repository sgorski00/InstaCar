package pl.studia.InstaCar.service.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.studia.InstaCar.model.builders.ActivationEmailBuilder;
import pl.studia.InstaCar.model.dto.EmailDto;
import pl.studia.InstaCar.service.event.UserRegistrationEvent;
import pl.studia.InstaCar.service.EmailService;

@Component
@Log4j2
public class UserRegistrationListener {

    @Value("${spring.mail.username}")
    private String contactEmail;

    private final EmailService emailService;

    @Autowired
    public UserRegistrationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener(condition = "#event.user.email != null")
    public void onUserRegistration(UserRegistrationEvent event) {
        log.info("New user has registered! Email sending...");
        String email = event.getUser().getEmail();
        String token = event.getEmailToken().getToken();

        EmailDto emailDto = ActivationEmailBuilder.build(contactEmail,email, token);
        emailService.sendEmail(emailDto);
    }
}
