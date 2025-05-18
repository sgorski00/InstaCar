package pl.studia.InstaCar.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "{NotBlank.firstName}")
    @Size(max = 50, message = "{Size.firstName}")
    private String firstName;

    @EqualsAndHashCode.Include
    @NotBlank(message = "{NotBlank.lastName}")
    @Size(max = 50, message = "{Size.lastName}")
    private String lastName;

    @EqualsAndHashCode.Include
    @Nullable
    @NotBlank(message = "{NotBlank.phoneNumber}")
    @Size(max = 50, message = "{Size.phoneNumber}")
    private String phoneNumber;

    @EqualsAndHashCode.Include
    @Nullable
    @NotBlank(message = "{NotBlank.address}")
    @Size(max = 255, message = "{Size.address}")
    private String address;

    @EqualsAndHashCode.Include
    @Nullable
    @NotBlank(message = "{NotBlank.city}")
    @Size(max = 50, message = "{Size.city}")
    private String city;

    @EqualsAndHashCode.Include
    @Nullable
    @NotBlank(message = "{NotBlank.postalCode}")
    @Size(max = 10, message = "{Size.postalCode}")
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
