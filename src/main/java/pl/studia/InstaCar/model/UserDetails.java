package pl.studia.InstaCar.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.studia.InstaCar.model.authentication.ApplicationUser;

import java.io.Serializable;

@Entity
@Table(name = "user_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ApplicationUser user;

    @Size(max = 50, message = "Imię nie może przekraczać 50 znaków")
    private String firstName;

    @Size(max = 50, message = "Nazwisko nie może przekraczać 50 znaków")
    private String lastName;

    @Nullable
    @Size(max = 50, message = "Numer telefonu nie może przekraczać 50 znaków")
    private String phoneNumber;

    @Nullable
    @Size(max = 255, message = "Adres nie może przekraczać 255 znaków")
    private String address;

    @Nullable
    @Size(max = 50, message = "Nazwa miasta nie może przekraczać 50 znaków")
    private String city;

    @Nullable
    @Size(max = 10, message = "Kod pocztowy nie może przekraczać 10 znaków")
    private String postalCode;

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }
}
