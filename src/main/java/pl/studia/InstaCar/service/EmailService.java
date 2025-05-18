package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.dto.EmailDto;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }


    public void sendEmail(EmailDto email) {
        SimpleMailMessage message = email.mapToSimpleMailMessage();
        mailSender.send(message);
    }
}
