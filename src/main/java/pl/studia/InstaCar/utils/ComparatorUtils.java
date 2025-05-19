package pl.studia.InstaCar.utils;

import pl.studia.InstaCar.model.Vehicle;

import java.util.Comparator;

public class ComparatorUtils {

    public static Comparator<Vehicle> getVehicleComparator(String field) {
        Comparator<Vehicle> comparator;

        if (field.equals("price")) {
            comparator = Comparator.comparingDouble(Vehicle::getPrice);
        } else {
            comparator = Comparator
                    .comparing((Vehicle v) -> v.getModel().getBrand(), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(v -> v.getModel().getModelName(), String.CASE_INSENSITIVE_ORDER);
        }

        return comparator;
    }
}
