package pl.studia.InstaCar.utils;

import pl.studia.InstaCar.model.Vehicle;

import java.util.Comparator;

public class ComparatorUtils {

    public static Comparator<Vehicle> getVehicleComparator(String field) {
        Comparator<Vehicle> comparator;

        switch (field) {
            case "price" -> comparator = Comparator.comparingDouble(Vehicle::getPrice);
            default -> comparator = Comparator
                    .comparing((Vehicle v) -> v.getModel().getBrand(), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(v -> v.getModel().getModelName(), String.CASE_INSENSITIVE_ORDER);
        }

        return comparator;
    }
}
