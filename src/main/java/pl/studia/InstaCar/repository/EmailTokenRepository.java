package pl.studia.InstaCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.studia.InstaCar.model.authentication.tokens.EmailActivationToken;

import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailActivationToken, Long> {
    Optional<EmailActivationToken> findFirstByTokenOrderByIdDesc(String token);
}
