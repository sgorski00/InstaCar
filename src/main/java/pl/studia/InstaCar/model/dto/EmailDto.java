package pl.studia.InstaCar.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.mail.SimpleMailMessage;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

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

    public SimpleMailMessage mapToSimpleMailMessage() {
        String text =
        """
        Message from: %s
        Date: %s
        Time: %s
        
        %s
        """.formatted(name, LocalDate.now(), LocalTime.now(), message);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(topic);
        mailMessage.setText(text);
        return mailMessage;
    }
}
