package pl.studia.InstaCar.service.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.studia.InstaCar.model.builders.ResetPasswordEmailBuilder;
import pl.studia.InstaCar.service.event.PasswordResetEvent;
import pl.studia.InstaCar.service.EmailService;

import java.net.UnknownHostException;

@Component
@Log4j2
public class PasswordResetListener {

    @Value("${spring.mail.username}")
    private String contactEmail;

    private final EmailService emailService;
    private final MessageSource messageSource;

    public PasswordResetListener(EmailService emailService, MessageSource messageSource) {
        this.emailService = emailService;
        this.messageSource = messageSource;
    }

    @Async
    @EventListener(condition = "#event.user.email != null")
    public void onUserRegistration(PasswordResetEvent event) {
        log.info("New password reset request! Email sending...");
        String token = event.getResetToken().getToken();
        String emailTo = event.getUser().getEmail();
        try {
            emailService.sendEmail(ResetPasswordEmailBuilder.build(contactEmail, emailTo, token, messageSource));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
