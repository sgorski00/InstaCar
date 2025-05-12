package pl.studia.InstaCar.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RentStatus {
    PENDING("Oczekuje na akceptacje"),
    ACTIVE("Aktywne"),
    CANCELLED("Anulowane"),
    FINISHED("Zakończone");

    private final String displayName;

    @Override
    public String toString() {
        return displayName;
    }
}
