package pl.studia.InstaCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.studia.InstaCar.model.CarModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {

    List<CarModel> findAllByModelName(String name);

    List<CarModel> findAllByBrand(String brand);
}
