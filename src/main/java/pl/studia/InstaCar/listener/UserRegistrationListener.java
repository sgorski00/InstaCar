package pl.studia.InstaCar.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.studia.InstaCar.event.UserRegistrationEvent;
import pl.studia.InstaCar.service.EmailService;

@Component
@Log4j2
public class UserRegistrationListener {

    private final EmailService emailService;

    @Autowired
    public UserRegistrationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener(condition = "#event.user.email != null")
    public void onUserRegistration(UserRegistrationEvent event) {
        log.info("New user has registered! Email sending...");
        String to = event.getUser().getEmail();
        String subject = "InstaCar - potwierdzenie rejestracji";
        String text = """
                Cześć, dziękujemy za rejestracje w serwisie InstaCar!\n\n
                Proszę potwierdzić swój adres email, żeby móc korzystać z serwisu.\n\n
                Twój link aktywacyjny:
                link
                """;
        emailService.sendEmail(to, subject, text);
    }
}
