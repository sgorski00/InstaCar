package pl.studia.InstaCar.service.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.studia.InstaCar.service.event.UserRegistrationEvent;
import pl.studia.InstaCar.service.EmailService;

@Component
@Log4j2
public class UserRegistrationListener {

    private final EmailService emailService;
    private final String serverAddress;

    @Autowired
    public UserRegistrationListener(EmailService emailService) {
        this.emailService = emailService;
        this.serverAddress = "localhost";
    }

    @Async
    @EventListener(condition = "#event.user.email != null")
    public void onUserRegistration(UserRegistrationEvent event) {
        log.info("New user has registered! Email sending...");
        String to = event.getUser().getEmail();
        String token = event.getEmailToken().getToken();
        String activationLink = "%s/activate?token=%s".formatted(serverAddress, token);
        String subject = "InstaCar - potwierdzenie rejestracji";
        String text = """
                Cześć, dziękujemy za rejestracje w serwisie InstaCar!
                
                Proszę potwierdzić swój adres email, żeby móc korzystać z serwisu.
                Twój link aktywacyjny:
                %s
                """.formatted(activationLink);
        emailService.sendEmail(to, subject, text);
    }
}
