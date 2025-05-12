package pl.studia.InstaCar.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FuelType {
    DIESEL("Diesel"),
    ETHANOL("Benzyna"),
    HYBRID("Hybryda"),
    LPG("LPG"),
    ELECTRIC("Elektryczny");

    private final String displayName;

    @Override
    public String toString() {
        return  displayName;
    }
}
