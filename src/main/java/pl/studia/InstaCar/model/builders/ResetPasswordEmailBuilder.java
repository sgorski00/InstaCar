package pl.studia.InstaCar.model.builders;

import org.springframework.beans.factory.annotation.Value;
import pl.studia.InstaCar.model.dto.EmailDto;

public class ResetPasswordEmailBuilder {

    @Value("${contact.email}")
    private static String contactEmail;

    public static EmailDto build(String email, String token) {
        String serverAddress = "localhost";
        String resetLink = "%s/login/reset/form?token=%s".formatted(serverAddress, token);
        String message = getMessage(resetLink);

        return EmailDto.builder()
                .emailFrom(contactEmail)
                .emailTo(email)
                .topic("InstaCar - resetowanie hasła")
                .message(message)
                .name("InstaCar - prosimy nie odpowiadać na tego maila.")
                .build();
    }

    private static String getMessage(String resetLink) {
        return """
                Cześć,
                
                w celu zresetowania hasła, wypełnij formularz znajdujący się w linku poniżej:
                %s
                """.formatted(resetLink);
    }
}
