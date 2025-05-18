package pl.studia.InstaCar.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.studia.InstaCar.model.enums.CarType;
import pl.studia.InstaCar.model.enums.FuelType;
import pl.studia.InstaCar.model.enums.Transmission;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "car_models")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{NotBlank.brand}")
    @Column(nullable = false)
    private String brand;

    @NotBlank(message = "{NotBlank.modelName}")
    @Column(nullable = false)
    private String modelName;

    @NotNull(message = "{NotNull.carType}")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarType carType;

    @Nullable
    @Min(value = 2, message = "{Min.seats}")
    @Max(value = 9, message = "{Max.seats}")
    private Integer seats;

    @Nullable
    @Min(value = 2, message = "{Min.doors}")
    @Max(value = 5, message = "{Max.doors}")
    private Integer doors;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    private Transmission transmission;

    @Column(nullable = false)
    private Timestamp createdAt;

    private Timestamp updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    @Override
    public String toString() {
        return this.brand + " " + this.modelName;
    }
}
