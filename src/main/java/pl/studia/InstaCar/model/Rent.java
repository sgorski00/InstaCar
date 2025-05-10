package pl.studia.InstaCar.model;

import jakarta.persistence.*;
import lombok.*;
import pl.studia.InstaCar.config.exceptions.StatusChangeException;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.enums.RentStatus;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "rents")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Rent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private LocalDate rentDate;

    private LocalDate returnDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pickup_city_id", nullable = false)
    private City pickUpCity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "return_city_id", nullable = false)
    private City returnCity;

    private double totalCost = 0;

    private String additionalInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentStatus rentStatus = RentStatus.PENDING;

    @Column(nullable = false)
    private Timestamp createdAt;

    private Timestamp statusChangedAt;

    @PrePersist
    public void setTotalCost() {
        this.createdAt = Timestamp.from(Instant.now());
        if(totalCost != 0) return;
        this.totalCost = vehicle.getPrice() * (returnDate.toEpochDay() - rentDate.toEpochDay() + 1);
    }

    public void setRentStatus(RentStatus status) throws StatusChangeException {
        if(this.rentStatus == status) return;
        if(this.rentStatus.equals(RentStatus.FINISHED)) throw new StatusChangeException("Zakończonego zamówienie nie można edytować.");
        this.rentStatus = status;
        this.statusChangedAt = Timestamp.from(Instant.now());
    }
}
