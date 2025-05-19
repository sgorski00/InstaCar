package pl.studia.InstaCar.model.builders;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import pl.studia.InstaCar.model.dto.EmailDto;

import java.net.InetAddress;

public class ActivationEmailBuilder {

    private static final String SERVER_ADDRESS = InetAddress.getLoopbackAddress().getHostName();

    public static EmailDto build(String emailFrom, String emailTo, String token, MessageSource messageSource) {
        String activationLink = "%s/activate?token=%s".formatted(SERVER_ADDRESS, token);
        String text = getMessage(activationLink, messageSource);
        return EmailDto.builder()
                .emailFrom(emailFrom)
                .emailTo(emailTo)
                .topic(messageSource.getMessage("mail.activation.title", null, LocaleContextHolder.getLocale()))
                .message(text)
                .name(messageSource.getMessage("mail.general.name", null, LocaleContextHolder.getLocale()))
                .build();
    }

    private static String getMessage(String activationLink, MessageSource messageSource) {
        return messageSource.getMessage("mail.activation.message", new Object[]{activationLink}, LocaleContextHolder.getLocale());
    }
}
