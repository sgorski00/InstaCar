package pl.studia.InstaCar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.studia.InstaCar.model.authentication.ApplicationUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUsernameIgnoreCase(String username);
    Optional<ApplicationUser> findByEmailIgnoreCase(String email);

    @Query("""
    SELECT u FROM ApplicationUser u
    WHERE (
        :#{#query} IS NULL OR
        LOWER(u.username) LIKE :#{#query} OR
        LOWER(u.email) LIKE :#{#query}
    )
""")
    Page<ApplicationUser> findAllBySearch(@Param("query") String query, Pageable pageable);

    boolean existsByEmailIgnoreCase(String email);
}
