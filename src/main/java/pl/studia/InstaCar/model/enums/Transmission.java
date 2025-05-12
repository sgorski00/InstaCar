package pl.studia.InstaCar.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Transmission {
    AUTOMATIC("Automatyczna"),
    MANUAL("Manulana");

    private final String displayName;

    @Override
    public String toString() {
        return displayName;
    }
}
