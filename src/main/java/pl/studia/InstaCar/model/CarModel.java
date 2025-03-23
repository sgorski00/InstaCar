package pl.studia.InstaCar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.studia.InstaCar.model.enums.CarType;
import pl.studia.InstaCar.model.enums.FuelType;
import pl.studia.InstaCar.model.enums.Transmission;

@Entity
@Table(name = "car_models")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String brand;

    @NotBlank
    private String modelName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CarType carType;

    @Min(2)
    @Max(9)
    private int seats;

    @Min(2)
    @Max(5)
    private int doors;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    private Transmission transmission;
}
