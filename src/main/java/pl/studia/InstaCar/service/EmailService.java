package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.dto.EmailDto;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, MessageSource messageSource) {
        this.mailSender = javaMailSender;
        this.messageSource = messageSource;
    }


    public void sendEmail(EmailDto email) {
        SimpleMailMessage message = email.mapToSimpleMailMessage(messageSource);
        mailSender.send(message);
    }
}
