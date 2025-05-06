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

    @NotBlank
    private String type;

    @NotNull
    private CarModel carModel = new CarModel();

    private SportCar sportCar;
    private CityCar cityCar;

    @NotNull
    @Size(max = 250)
    private String description = "No description yet.";
}
