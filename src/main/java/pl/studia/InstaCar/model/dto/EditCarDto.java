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

    @NotNull(message = "Model nie może być pusty")
    @Valid
    private CarModel model;

    @NotBlank(message = "Typ pojazdu nie może być pusty")
    private String carType;

    @Size(min=3, max=10, message = "Numer rejestracyjny musi mieć od 3 do 10 znaków")
    private String licensePlate;

    @NotBlank(message = "VIN nie może być pusty")
    private String vin;

    private String color;

    @Min(value = 1950, message = "Rok produkcji nie może być mniejszy niż 1950")
    @Max(value = 2030, message = "Rok produkcji nie może być większy niż 2030")
    private int productionYear;

    @Positive(message = "Cena musi być większa od 0")
    private double price;

    @NotBlank(message = "Pole silnik musi być wypełnione")
    private String engine;

    @NotBlank(message = "Należy załączyć zdjęcie")
    private String imageUrl;

    @Nullable
    @Positive(message = "Przyspieszenie nie może być mniejsze od 0")
    private Double acceleration;

    @Nullable
    @Min(value = 100,message = "Prędkość minimalna to 100 km/h")
    @Max(value = 400,message = "Prędkość minimalna to 400 km/h")
    private Integer topSpeed;

    @Nullable
    @Min(value = 50,message = "Minimalna ilość koni mechanicznych to 50")
    @Max(value = 1000,message = "Maksymalna ilość koni mechanicznych to 1000")
    private Integer horsePower;

    @Nullable
    @Positive(message = "Pojemność bagażnika musi być dodatnia")
    private Double trunkCapacity;

    @NotBlank(message = "Opis nie może być pusty")
    @Size(max = 250, message = "Opis nie może mieć więcej niż 250 znaków")
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
