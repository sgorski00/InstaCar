package pl.studia.InstaCar.service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.studia.InstaCar.model.Rent;

@Getter
public class OrderCreateEvent extends ApplicationEvent {

    private final Rent rent;

    public OrderCreateEvent(Object source, Rent rent) {
        super(source);
        this.rent = rent;
    }
}
