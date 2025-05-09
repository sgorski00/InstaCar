package pl.studia.InstaCar.model;

import java.time.LocalDate;

public interface Rental {
    boolean isAvailable(LocalDate startDate, LocalDate endDate);

    void rent(Rent rent);
}
