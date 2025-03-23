package pl.studia.InstaCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.studia.InstaCar.model.CarModel;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {
}
