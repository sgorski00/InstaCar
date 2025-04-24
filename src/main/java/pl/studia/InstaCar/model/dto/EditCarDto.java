package pl.studia.InstaCar.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.studia.InstaCar.config.exceptions.EntityValidationException;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.model.CityCar;
import pl.studia.InstaCar.model.SportCar;
import pl.studia.InstaCar.model.Vehicle;

@Data
@NoArgsConstructor(force = true)
public class EditCarDto {

    private Long id;
    private CarModel model;
    private String carType;
    private String licensePlate;
    private String vin;
    private String color;
    private int productionYear;
    private double price;
    private String engine;
    private String imageUrl;
    private double acceleration;
    private int topSpeed;
    private int horsePower;
    private double trunkCapacity;

    public EditCarDto(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.carType = vehicle instanceof SportCar ? "SPORT" : "CITY";
        this.model = vehicle.getModel();
        this.licensePlate = vehicle.getLicensePlate();
        this.vin = vehicle.getVin();
        this.color = vehicle.getColor();
        this.productionYear = vehicle.getProductionYear();
        this.price = vehicle.getPrice();
        this.engine = vehicle.getEngine();
        this.imageUrl = vehicle.getImageUrl();
        if (vehicle instanceof SportCar sportCar) {
            this.acceleration = sportCar.getAcceleration();
            this.topSpeed = sportCar.getTopSpeed();
            this.horsePower = sportCar.getHorsePower();
        }
        if (vehicle instanceof CityCar cityCar) {
            this.trunkCapacity = cityCar.getTrunkCapacity();
        }
    }

    public Vehicle mapToCar(Vehicle vehicle) {
        vehicle.setModel(model);
        vehicle.setLicensePlate(licensePlate);
        vehicle.setVin(vin);
        vehicle.setColor(color);
        vehicle.setProductionYear(productionYear);
        vehicle.setPrice(price);
        vehicle.setEngine(engine);
        vehicle.setImageUrl(imageUrl);
        return switch (carType.toUpperCase()) {
            case "SPORT" -> {
                SportCar sportCar = (SportCar) vehicle;
                sportCar.setAcceleration(acceleration);
                sportCar.setHorsePower(horsePower);
                sportCar.setTopSpeed(topSpeed);
                yield sportCar;
            }
            case "CITY" -> {
                CityCar cityCar = (CityCar) vehicle;
                cityCar.setTrunkCapacity(trunkCapacity);
                yield cityCar;
            }
            default -> throw new EntityValidationException("Zły typ pojazdu!", "/admin/cars/edit/" + id);
        };
    }
}
