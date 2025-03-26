package pl.studia.InstaCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.studia.InstaCar.model.CityCar;

@Repository
public interface CityCarRepository extends JpaRepository<CityCar, Long> {
}
