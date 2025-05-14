package pl.studia.InstaCar.model.dto;

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

    private String emailFrom;
    private String emailTo;
    private String name;
    private String topic;
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
