package pl.studia.InstaCar.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "cities")
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "pickUpCity")
    private List<Rent> pickUps;

    @OneToMany(mappedBy = "returnCity")
    private List<Rent> returns;
}
