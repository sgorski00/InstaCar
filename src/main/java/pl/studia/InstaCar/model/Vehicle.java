package pl.studia.InstaCar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import pl.studia.InstaCar.model.enums.RentalStatus;

import java.io.Serializable;
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
    private CarModel model;

    @Column(unique = true)
    private String licensePlate;

    @Column(unique = true, nullable = false)
    private String vin;

    private int productionYear;

    private String engine;

    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "rent_status")
    private RentalStatus status = RentalStatus.AVAILABLE;

    @Positive(message = "Cena musi być większa od 0")
    private double price; // per day in PLN

    private String imageUrl;

    @NotNull(message = "Opis nie możeb być pusty")
    @Column(nullable = false)
    private String description = "No description yet.";

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rent> rents = new ArrayList<>();

    @Override
    public boolean isAvailable() {
        return status.equals(RentalStatus.AVAILABLE);
    }

    @Override
    public boolean isAvailableInDate(LocalDate startDate, LocalDate endDate) {
        if (rents == null || rents.isEmpty()) return true;
        return rents.stream()
                .noneMatch(rent -> rent.getRentDate().isBefore(endDate)
                        && rent.getReturnDate().isAfter(startDate)
                );
    }

    @Override
    public void rent() {
        if (isAvailable()) {
            this.status = RentalStatus.RENTED;
        } else {
            throw new IllegalStateException("Vehicle is not available for rent");
        }
    }

    @Override
    public void returnRental() {
        if (status.equals(RentalStatus.RENTED)) {
            this.status = RentalStatus.AVAILABLE;
        } else {
            throw new IllegalStateException("Vehicle is not rented");
        }
    }

    public String getImageUrl() {
        return Objects.requireNonNullElse(imageUrl, "/images/no_image.png");
    }
}
