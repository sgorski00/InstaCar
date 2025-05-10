package pl.studia.InstaCar.model.builders;

import pl.studia.InstaCar.model.Rent;
import pl.studia.InstaCar.model.dto.EmailDto;

public class OrderCreateEmailBuilder {

    public static EmailDto build(String emailFrom, Rent rent) {
        return EmailDto.builder()
                .emailFrom(emailFrom)
                .emailTo(rent.getUser().getEmail())
                .name("InstaCar - prosimy nie odpowiadać na tego maila.")
                .topic("InstaCar - potwierdzenie zamówienia nr %s".formatted(rent.getId()))
                .message(getMessage(rent))
                .build();
    }

    private static String getMessage(Rent rent) {
        return """
        Twoje zamówienie o numerze %s zostało przyjęte.
        
        Prosimy o potwierdzenie rezerwacji pojazdu %s na swoim profilu. Łączna cena zamówienia to: %f zł.
        Jeżeli zamówienie zostało już potwierdzone, proszę o zignorowanie wiadomości.
        
        Dziękujemy za skorzystanie z naszych usług,
        InstaCar.
        """.formatted(rent.getId(), rent.getVehicle().getModel(), rent.getTotalCost());
    }
}
