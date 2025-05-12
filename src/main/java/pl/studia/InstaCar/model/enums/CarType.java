package pl.studia.InstaCar.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CarType {
    SEDAN("Sedan"),
    HATCHBACK("Hatchback"),
    SUV("SUV"),
    COUPE("Coupe"),
    CABRIOLET("Cabriolet"),
    WAGON("Wagon"),
    VAN("Van"),
    MINIVAN("Minivan"),
    PICKUP("Pickup");

    private final String displayName;

    @Override
    public String toString() {
        return displayName;
    }
}
