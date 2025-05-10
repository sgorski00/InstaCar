package pl.studia.InstaCar.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.studia.InstaCar.model.authentication.ApplicationUser;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "user_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ApplicationUser user;

    @EqualsAndHashCode.Include
    @Size(max = 50, message = "Imię nie może przekraczać 50 znaków")
    private String firstName;

    @EqualsAndHashCode.Include
    @Size(max = 50, message = "Nazwisko nie może przekraczać 50 znaków")
    private String lastName;

    @EqualsAndHashCode.Include
    @Nullable
    @Size(max = 50, message = "Numer telefonu nie może przekraczać 50 znaków")
    private String phoneNumber;

    @EqualsAndHashCode.Include
    @Nullable
    @Size(max = 255, message = "Adres nie może przekraczać 255 znaków")
    private String address;

    @EqualsAndHashCode.Include
    @Nullable
    @Size(max = 50, message = "Nazwa miasta nie może przekraczać 50 znaków")
    private String city;

    @EqualsAndHashCode.Include
    @Nullable
    @Size(max = 10, message = "Kod pocztowy nie może przekraczać 10 znaków")
    private String postalCode;

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
        return this.firstName + " " + this.lastName;
    }
}
