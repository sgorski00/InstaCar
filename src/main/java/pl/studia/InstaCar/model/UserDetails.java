package pl.studia.InstaCar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.studia.InstaCar.model.authentication.ApplicationUser;

@Entity
@Table(name = "user_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ApplicationUser user;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(max = 50)
    private String phoneNumber;

    @Size(max = 255)
    private String address;

    @Size(max = 50)
    private String city;

    @Size(max = 10)
    private String postalCode;
}
