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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewCarDto {

    @NotBlank(message = "Typ pojazdu nie może być pusty")
    private String type;

    @NotNull(message = "Model pojazdu nie może być pusty")
    private CarModel carModel = new CarModel();

    private SportCar sportCar;
    private CityCar cityCar;

    @NotNull(message = "Opis nie może być pusty")
    @Size(max = 250, message = "Opis nie może mieć więcej niż 250 znaków")
    private String description = "No description yet.";
}
