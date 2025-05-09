package pl.studia.InstaCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.studia.InstaCar.model.Rent;
import pl.studia.InstaCar.model.authentication.ApplicationUser;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {

    List<Rent> findAllByUserOrderByRentDateDesc(ApplicationUser user);
}
