package pl.studia.InstaCar.utils;

import org.junit.jupiter.api.Test;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.model.SportCar;
import pl.studia.InstaCar.model.Vehicle;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComparatorUtilsTest {

    private Vehicle createVehicle(String brand, String modelName, double price) {
        CarModel model = new CarModel();
        model.setBrand(brand);
        model.setModelName(modelName);

        Vehicle vehicle = new SportCar();
        vehicle.setModel(model);
        vehicle.setPrice(price);

        return vehicle;
    }

    @Test
    void shouldSortByPrice() {
        Vehicle v1 = createVehicle("Audi", "A4", 30000);
        Vehicle v2 = createVehicle("BMW", "320", 25000);
        Vehicle v3 = createVehicle("Mercedes", "C", 35000);

        List<Vehicle> vehicles = Arrays.asList(v1, v2, v3);
        vehicles.sort(ComparatorUtils.getVehicleComparator("price"));

        assertEquals(v2, vehicles.get(0));
        assertEquals(v1, vehicles.get(1));
        assertEquals(v3, vehicles.get(2));
    }

    @Test
    void shouldSortByBrandAndModelNameWhenFieldIsUnknown() {
        Vehicle v1 = createVehicle("Audi", "A3", 30000);
        Vehicle v2 = createVehicle("BMW", "320", 25000);
        Vehicle v3 = createVehicle("Audi", "A1", 35000);

        List<Vehicle> vehicles = Arrays.asList(v1, v2, v3);
        vehicles.sort(ComparatorUtils.getVehicleComparator("unknown"));

        assertEquals(v3, vehicles.get(0));
        assertEquals(v1, vehicles.get(1));
        assertEquals(v2, vehicles.get(2));
    }
}
