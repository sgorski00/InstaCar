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

    @Min(value = 50,message = "Minimalna ilość koni mechanicznych to 50")
    @Max(value = 1000,message = "Maksymalna ilość koni mechanicznych to 1000")
    private Integer horsePower;

    @Positive(message = "Przyspieszenie nie może być mniejsze od 0")
    private Double acceleration; // 0-100 km/h in seconds

    @Min(value = 100,message = "Prędkość minimalna to 100 km/h")
    @Max(value = 400,message = "Prędkość minimalna to 400 km/h")
    private Integer topSpeed;
}
