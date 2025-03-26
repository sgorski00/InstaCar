package pl.studia.InstaCar.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    private int horsePower;
    private double acceleration; // 0-100 km/h in seconds
    private int topSpeed;
}
