package pl.studia.InstaCar.model.dto;

import lombok.Data;
import pl.studia.InstaCar.model.Vehicle;

@Data
public class CarUpdateDto {
    private String color;
    private String engine;
    private String licensePlate;
    private double price;
    private int productionYear;
    private String vin;

    public void updateCar(Vehicle car) {
        car.setColor(color);
        car.setEngine(engine);
        car.setLicensePlate(licensePlate);
        car.setPrice(price);
        car.setProductionYear(productionYear);
        car.setVin(vin);
    }
}
