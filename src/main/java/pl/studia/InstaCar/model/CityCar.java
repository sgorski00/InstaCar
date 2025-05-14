package pl.studia.InstaCar.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "city_cars")
@DiscriminatorValue("CITY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityCar extends Vehicle {

    @Positive(message = "Pojemność bagażnika musi być dodatnia")
    private Double trunkCapacity;
}
