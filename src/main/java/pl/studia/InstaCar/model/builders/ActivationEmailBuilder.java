package pl.studia.InstaCar.model.builders;

import pl.studia.InstaCar.model.dto.EmailDto;

public class ActivationEmailBuilder {

    public static EmailDto build(String emailFrom, String emailTo, String token) {
        String serverAddress = "localhost";
        String activationLink = "%s/activate?token=%s".formatted(serverAddress, token);
        String text = getMessage(activationLink);
        return EmailDto.builder()
                .emailFrom(emailFrom)
                .emailTo(emailTo)
                .topic("InstaCar - potwierdzenie rejestracji")
                .message(text)
                .name("InstaCar - prosimy nie odpowiadać na tego maila.")
                .build();
    }

    private static String getMessage(String activationLink) {
        return """
                Cześć, dziękujemy za rejestracje w serwisie InstaCar!
                
                Proszę potwierdzić swój adres email, żeby móc korzystać z serwisu.
                Twój link aktywacyjny:
                %s
                """.formatted(activationLink);
    }
}
