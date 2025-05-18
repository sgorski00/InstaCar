package pl.studia.InstaCar.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.model.CityCar;
import pl.studia.InstaCar.model.SportCar;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewCarDto implements Serializable {

    @NotBlank(message = "{NotBlank.carType}")
    private String carType;

    @NotNull(message = "{NotNull.model}")
    private CarModel carModel = new CarModel();

    private SportCar sportCar = new SportCar();
    private CityCar cityCar = new CityCar();

    @NotBlank(message = "{NotBlank.licensePlate}")
    @Size(max = 250, message = "{Size.licensePlate}")
    private String description = "No description yet.";
}
