package pl.studia.InstaCar.service.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.studia.InstaCar.model.builders.OrderCreateEmailBuilder;
import pl.studia.InstaCar.service.EmailService;
import pl.studia.InstaCar.service.event.OrderCreateEvent;

@Component
@Log4j2
public class OrderCreateListener {

    @Value("${spring.mail.username}")
    private String contactEmail;

    private final EmailService emailService;
    private final MessageSource messageSource;

    @Autowired
    public OrderCreateListener(EmailService emailService, MessageSource messageSource) {
        this.emailService = emailService;
        this.messageSource = messageSource;
    }

    @Async
    @EventListener
    public void onOrderCreate(OrderCreateEvent event) {
        log.info("New order has been created! Email sending...");
        emailService.sendEmail(OrderCreateEmailBuilder.build(contactEmail, event.getRent(), messageSource));
    }
}
