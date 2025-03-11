package pl.studia.InstaCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.studia.InstaCar.model.authentication.tokens.PasswordResetToken;

import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findFirstByTokenOrderByIdDesc(String token);
}
