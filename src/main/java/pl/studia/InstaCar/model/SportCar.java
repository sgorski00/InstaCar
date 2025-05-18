package pl.studia.InstaCar.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sport_cars")
@DiscriminatorValue("SPORT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SportCar extends Vehicle {

    @Min(value = 50, message = "{Min.horsePower}")
    @Max(value = 1000, message = "{Max.horsePower}")
    private Integer horsePower;

    @Positive(message = "{Positive.acceleration}")
    private Double acceleration; // 0-100 km/h in seconds

    @Min(value = 100, message = "{Min.topSpeed}")
    @Max(value = 400, message = "{Max.topSpeed}")
    private Integer topSpeed;
}
