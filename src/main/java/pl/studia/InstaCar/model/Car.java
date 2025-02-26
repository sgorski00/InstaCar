package pl.studia.InstaCar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
public class Car {

    public Car() {
    }

    public Car(String model, Long id) {
        this.model = model;
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String model;

    public Long id() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String model() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
