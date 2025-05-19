package pl.studia.InstaCar.model.builders;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import pl.studia.InstaCar.model.dto.EmailDto;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Log4j2
public class ResetPasswordEmailBuilder {

    private static final String SERVER_ADDRESS = InetAddress.getLoopbackAddress().getHostName();

    public static EmailDto build(String emailFrom, String emailTo, String token, MessageSource messageSource) throws UnknownHostException {
        String resetLink = "%s/login/reset/form?token=%s".formatted(SERVER_ADDRESS, token);
        String message = getMessage(resetLink, messageSource);

        return EmailDto.builder()
                .emailFrom(emailFrom)
                .emailTo(emailTo)
                .topic(messageSource.getMessage("mail.reset.title", null, LocaleContextHolder.getLocale()))
                .message(message)
                .name(messageSource.getMessage("mail.general.name", null, LocaleContextHolder.getLocale()))
                .build();
    }

    private static String getMessage(String resetLink, MessageSource messageSource) {
        return messageSource.getMessage("mail.reset.message", new Object[]{resetLink}, LocaleContextHolder.getLocale());
    }
}
