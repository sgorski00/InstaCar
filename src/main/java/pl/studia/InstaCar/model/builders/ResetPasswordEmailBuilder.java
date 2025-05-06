package pl.studia.InstaCar.model.builders;

import pl.studia.InstaCar.model.dto.EmailDto;

public class ResetPasswordEmailBuilder {

    public static EmailDto build(String emailFrom, String emailTo, String token) {
        String serverAddress = "localhost";
        String resetLink = "%s/login/reset/form?token=%s".formatted(serverAddress, token);
        String message = getMessage(resetLink);

        return EmailDto.builder()
                .emailFrom(emailFrom)
                .emailTo(emailTo)
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
