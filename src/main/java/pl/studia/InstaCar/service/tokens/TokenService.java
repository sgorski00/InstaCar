package pl.studia.InstaCar.service.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.AuthProvider;
import pl.studia.InstaCar.model.authentication.tokens.BaseToken;

public abstract class TokenService<T extends BaseToken> {

    protected final JpaRepository<T, Long> tokenRepository;

    protected TokenService(JpaRepository<T, Long> tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void setTokenActivated(String token) {
        T foundToken = findLastToken(token);

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
        if(!user.getProvider().equals(AuthProvider.LOCAL)){
            throw new IllegalArgumentException("Zmiana hasła niemożliwa");
        }
        T token = generateTokenForUser(user);
        return tokenRepository.save(token);
    }

    protected abstract T generateTokenForUser(ApplicationUser user);
    protected abstract T findLastToken(String tokenStr);
}
