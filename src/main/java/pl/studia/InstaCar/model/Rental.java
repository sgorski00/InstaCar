package pl.studia.InstaCar.model;

import org.springframework.context.MessageSource;

import java.time.LocalDate;

public interface Rental {
    boolean isAvailable(LocalDate startDate, LocalDate endDate);

    void rent(Rent rent, MessageSource messageSource);
}
