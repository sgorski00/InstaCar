package pl.studia.InstaCar.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto implements Serializable {

    @Email(message = "{Email.emailFrom}")
    private String emailFrom;
    @Email(message = "{Email.emailTo}")
    private String emailTo;
    @Size(min = 2, max = 50, message = "{Size.name}")
    private String name;
    @Size(min = 2, max = 50, message = "{Size.topic}")
    private String topic;
    @Size(min = 2, max = 1000, message = "{Size.message}")
    private String message;

    public SimpleMailMessage mapToSimpleMailMessage(MessageSource messageSource) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String from = messageSource.getMessage("mail.general.from", new Object[]{name}, LocaleContextHolder.getLocale());
        String date = messageSource.getMessage("mail.general.date", new Object[]{LocalDate.now()}, LocaleContextHolder.getLocale());
        String time = messageSource.getMessage("mail.general.time", new Object[]{LocalTime.now().format(formatter)}, LocaleContextHolder.getLocale());
        String text = """
        %s
        %s
        %s
        
        %s
        """.formatted(from, date, time, message);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(topic);
        mailMessage.setText(text);
        return mailMessage;
    }
}
