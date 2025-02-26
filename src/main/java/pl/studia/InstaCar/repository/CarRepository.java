package pl.studia.InstaCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.studia.InstaCar.model.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
