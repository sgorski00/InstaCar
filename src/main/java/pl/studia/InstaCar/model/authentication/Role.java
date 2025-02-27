package pl.studia.InstaCar.model.authentication;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<ApplicationUser> users;

    @Override
    public String toString() {
        return name.toUpperCase().charAt(0) + name.substring(1).toLowerCase();
    }
}
