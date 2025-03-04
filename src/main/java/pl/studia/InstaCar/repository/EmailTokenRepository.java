package pl.studia.InstaCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.studia.InstaCar.model.authentication.EmailToken;

import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {
    Optional<EmailToken> findFirstByTokenOrderByIdDesc(String token);
}
