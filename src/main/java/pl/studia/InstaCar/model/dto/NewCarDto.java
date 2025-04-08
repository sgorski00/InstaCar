package pl.studia.InstaCar.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.model.CityCar;
import pl.studia.InstaCar.model.SportCar;
import pl.studia.InstaCar.model.Vehicle;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewCarDto {

    @NotBlank
    private String type;

    @NotNull
    private CarModel carModel;

    private SportCar sportCar;
    private CityCar cityCar;
}
