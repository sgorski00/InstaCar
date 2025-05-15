package pl.studia.InstaCar.model.dto;

import jakarta.validation.Valid;
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

    @NotBlank(message = "Typ pojazdu nie może być pusty")
    private String type;

    @NotNull(message = "Model pojazdu nie może być pusty")
    private CarModel carModel = new CarModel();

    private SportCar sportCar = new SportCar();
    private CityCar cityCar = new CityCar();

    @NotBlank(message = "Opis nie może być pusty")
    @Size(max = 250, message = "Opis nie może mieć więcej niż 250 znaków")
    private String description = "No description yet.";
}
