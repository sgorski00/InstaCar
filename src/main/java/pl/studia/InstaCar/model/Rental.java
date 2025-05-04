package pl.studia.InstaCar.model;

import java.time.LocalDate;

public interface Rental {
    boolean isAvailable();
    boolean isAvailableInDate(LocalDate startDate, LocalDate endDate);
    void rent();
    void returnRental();
}
