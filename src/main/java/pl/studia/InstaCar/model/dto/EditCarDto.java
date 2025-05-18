package pl.studia.InstaCar.model.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.model.CityCar;
import pl.studia.InstaCar.model.SportCar;
import pl.studia.InstaCar.model.Vehicle;

import java.io.Serializable;

@Data
@NoArgsConstructor(force = true)
public class EditCarDto implements Serializable {

    private Long id;

    @NotNull(message = "{NotNull.model}")
    @Valid
    private CarModel model;

    @NotBlank(message = "{NotBlank.carType}")
    private String carType;

    @Size(min=3, max=10, message = "{Size.licensePlate}")
    private String licensePlate;

    @NotBlank(message = "{NotBlank.vin}")
    private String vin;

    private String color;

    @Min(value = 1950, message = "{Min.productionYear}")
    @Max(value = 2030, message = "{Max.productionYear}")
    private int productionYear;

    @Positive(message = "{Positive.price}")
    private double price;

    @NotBlank(message = "{NotBlank.engine}")
    private String engine;

    @NotBlank(message = "{NotBlank.imageUrl}")
    private String imageUrl;

    @Nullable
    @Positive(message = "{Positive.acceleration}")
    private Double acceleration;

    @Nullable
    @Min(value = 100, message = "{Min.topSpeed}")
    @Max(value = 400, message = "{Max.topSpeed}")
    private Integer topSpeed;

    @Nullable
    @Min(value = 50, message = "{Min.horsePower}")
    @Max(value = 1000, message = "{Max.horsePower}")
    private Integer horsePower;

    @Nullable
    @Positive(message = "{Positive.trunkCapacity}")
    private Double trunkCapacity;

    @NotBlank(message = "{NotBlank.description}")
    @Size(max = 250, message = "{Size.description}")
    private String description;

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
        this.description = vehicle.getDescription();
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
        vehicle.setDescription(description);
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
            default ->  null;
        };
    }
}
