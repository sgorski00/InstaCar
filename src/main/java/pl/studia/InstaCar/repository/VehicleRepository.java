package pl.studia.InstaCar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.studia.InstaCar.model.Vehicle;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    @Query("""
        SELECT v FROM Vehicle v
        WHERE LOWER(v.model.brand) LIKE %:query% OR
        LOWER(v.model.modelName) LIKE %:query% OR
        LOWER(CONCAT(v.model.brand,' ',v.model.modelName)) LIKE %:query%
        """)
    List<Vehicle> findAllByQuery(@Param("query") String query);
}
