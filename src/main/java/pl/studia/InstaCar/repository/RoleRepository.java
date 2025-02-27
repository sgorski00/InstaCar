package pl.studia.InstaCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.studia.InstaCar.model.authentication.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNameIgnoreCase(String name);
}
