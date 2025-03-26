package pl.studia.InstaCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.studia.InstaCar.model.SportCar;

@Repository
public interface SportCarRepository extends JpaRepository<SportCar, Long> {
}
