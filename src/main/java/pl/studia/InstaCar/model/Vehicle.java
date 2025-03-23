package pl.studia.InstaCar.model;

import jakarta.persistence.*;
import lombok.*;
import pl.studia.InstaCar.model.enums.RentalStatus;

import java.util.List;

@Entity
@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "vehicle_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Vehicle implements Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id", nullable = false)
    private CarModel model;

    private String licensePlate;

    private String vin;

    private int productionYear;

    private String engine;

    private int mileage;

    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "rent_status")
    private RentalStatus status;

    private double price; // per day

    private String imageUrl;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Rent> rents;

    @Override
    public boolean isAvailable() {
        return status.equals(RentalStatus.AVAILABLE);
    }

    @Override
    public void rent() {
        if(isAvailable()) {
            this.status = RentalStatus.RENTED;
        } else {
            throw new IllegalStateException("Vehicle is not available for rent");
        }
    }

    @Override
    public void returnRental() {
        if(status.equals(RentalStatus.RENTED)){
            this.status = RentalStatus.AVAILABLE;
        } else {
            throw new IllegalStateException("Vehicle is not rented");
        }
    }
}
