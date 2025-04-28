package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.dto.EmailDto;

@Service
public class EmailService {

    private final MailSender mailSender;

    @Autowired
    public EmailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(EmailDto email) {
        SimpleMailMessage message = email.mapToSimpleMailMessage();
        mailSender.send(message);
    }
}
