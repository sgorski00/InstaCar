package pl.studia.InstaCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.studia.InstaCar.model.UserDetails;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
}
