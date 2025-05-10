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

    @NotBlank(message = "Marka nie może być pusta")
    @Column(nullable = false)
    private String brand;

    @NotBlank(message = "Model nie może być pusty")
    @Column(nullable = false)
    private String modelName;

    @NotNull(message = "Typ samochodu nie może być pusty")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarType carType;

    @Nullable
    @Min(value = 2, message = "Minimalna liczba miejsc to 2")
    @Max(value = 9, message = "Maksymalna liczba miejsc to 9")
    private Integer seats;

    @Nullable
    @Min(value = 2, message = "Minimalna liczba drzwi to 2")
    @Max(value = 5, message = "Maksymalna liczba drzwi to 5")
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
