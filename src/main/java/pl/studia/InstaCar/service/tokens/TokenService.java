package pl.studia.InstaCar.service.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.BaseToken;

import java.util.NoSuchElementException;

public abstract class TokenService<T extends BaseToken> {

    protected final JpaRepository<T, Long> tokenRepository;

    protected TokenService(JpaRepository<T, Long> tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void setTokenActivated(String token) {
        T foundToken = tokenRepository.findAll()
                .stream()
                .filter(t -> t.getToken().equals(token))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Podany token nie istnieje"));

        if(foundToken.getIsUsed()){
            throw new IllegalArgumentException("Podany token został już zużyty");
        }
        if(foundToken.isExpired()){
            throw new IllegalArgumentException("Podany token jest przeterminowany");
        }
        foundToken.setIsUsed(true);
        tokenRepository.save(foundToken);
    }

    @Transactional
    public T saveTokenForUser(ApplicationUser user) {
        if (user == null) {
            throw new IllegalArgumentException("Nie podano użytkownika");
        }
        T token = generateTokenForUser(user);
        return tokenRepository.save(token);
    }

    protected abstract T generateTokenForUser(ApplicationUser user);
}
