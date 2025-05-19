package pl.studia.InstaCar.model.builders;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import pl.studia.InstaCar.model.Rent;
import pl.studia.InstaCar.model.dto.EmailDto;

public class OrderCreateEmailBuilder {

    public static EmailDto build(String emailFrom, Rent rent, MessageSource messageSource) {
        return EmailDto.builder()
                .emailFrom(emailFrom)
                .emailTo(rent.getUser().getEmail())
                .name(messageSource.getMessage("mail.general.name", null, LocaleContextHolder.getLocale()))
                .topic(messageSource.getMessage("mail.order.create.title", new Object[]{rent.getId()}, LocaleContextHolder.getLocale()))
                .message(getMessage(rent, messageSource))
                .build();
    }

    private static String getMessage(Rent rent, MessageSource messageSource) {
        Object[] args = new Object[]{rent.getId(), rent.getVehicle().getModel(), rent.getTotalCost()};
        return messageSource.getMessage("mail.order.create.message", args, LocaleContextHolder.getLocale());
    }
}
