package pl.studia.InstaCar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.studia.InstaCar.model.Rent;
import pl.studia.InstaCar.model.authentication.ApplicationUser;

import java.time.LocalDate;
import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {

    Page<Rent> findAllByRentDateBetweenOrderByRentDateDesc(LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<Rent> findAllByUserOrderByRentDateDesc(ApplicationUser user);
}
