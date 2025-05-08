package pl.studia.InstaCar.model.authentication;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "roles")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazwa jest wymagana")
    @Column(name = "name", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<ApplicationUser> users;

    @Override
    public String toString() {
        return name.toUpperCase().charAt(0) + name.substring(1).toLowerCase();
    }
}
