package pl.studia.InstaCar.service.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.studia.InstaCar.service.event.PasswordResetEvent;
import pl.studia.InstaCar.service.EmailService;

@Component
@Log4j2
public class PasswordResetListener {

    private final EmailService emailService;
    private final String serverAddress;

    public PasswordResetListener(EmailService emailService) {
        this.emailService = emailService;
        this.serverAddress = "localhost";
    }

    @Async
    @EventListener(condition = "#event.user.email != null")
    public void onUserRegistration(PasswordResetEvent event) {
        log.info("New password reset request! Email sending...");
        String to = event.getUser().getEmail();
        String token = event.getResetToken().getToken();
        String resetLink = "%s/login/reset/form?token=%s".formatted(serverAddress, token);
        String subject = "InstaCar - resetowanie hasła";
        String text = """
                Cześć,
                
                w celu zresetowania hasła, wypełnij formularz znajdujący się w linku poniżej:
                %s
                """.formatted(resetLink);
        emailService.sendEmail(to, subject, text);
    }
}
