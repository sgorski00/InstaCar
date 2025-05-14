package pl.studia.InstaCar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import pl.studia.InstaCar.config.exceptions.NotAvailableException;
import pl.studia.InstaCar.model.enums.RentStatus;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "vehicle_type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SportCar.class, name = "SPORT"),
        @JsonSubTypes.Type(value = CityCar.class, name = "CITY")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Vehicle implements Rental, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id", nullable = false)
    @NotNull(message = "Model nie może być pusty")
    private CarModel model;

    @Column(unique = true)
    @Size(min=3, max=10, message = "Numer rejestracyjny musi mieć od 3 do 10 znaków")
    private String licensePlate;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "VIN nie może być pusty")
    private String vin;

    @Min(value = 1950, message = "Rok produkcji nie może być mniejszy niż 1950")
    @Max(value = 2030, message = "Rok produkcji nie może być większy niż 2030")
    private int productionYear;

    @NotBlank(message = "Pole silnik musi być wypełnione")
    private String engine;

    private String color;

    @Positive(message = "Cena musi być większa od 0")
    private double price; // per day in PLN

    @NotBlank(message = "Należy załączyć zdjęcie")
    private String imageUrl;

    @NotBlank(message = "Opis nie może być pusty")
    @Size(max = 250, message = "Opis nie może mieć więcej niż 250 znaków")
    @Column(nullable = false)
    private String description = "No description yet.";

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rent> rents = new ArrayList<>();

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
    public boolean isAvailable(LocalDate startDate, LocalDate endDate) {
        if (rents == null || rents.isEmpty()) return true;
        return rents.stream()
                .filter(rent -> rent.getRentStatus() == RentStatus.ACTIVE || rent.getRentStatus() == RentStatus.PENDING)
                .noneMatch(rent -> rent.getRentDate().isBefore(endDate)
                        && rent.getReturnDate().isAfter(startDate)
                );
    }

    @Override
    public void rent(Rent rent) {
        if (isAvailable(rent.getRentDate(), rent.getReturnDate())) {
            this.rents.add(rent);
        } else {
            throw new NotAvailableException("Vehicle is not available for rent");
        }
    }

    public String getImageUrl() {
        return Objects.requireNonNullElse(imageUrl, "/images/no_image.png");
    }
}
