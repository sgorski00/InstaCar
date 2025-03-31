package pl.studia.InstaCar.service.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.InstaCar.config.exceptions.TokenIllegalArgumentException;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.AuthProvider;
import pl.studia.InstaCar.model.authentication.tokens.BaseToken;

public abstract class TokenService<T extends BaseToken> {

    private final Class<T> type;
    protected final JpaRepository<T, Long> tokenRepository;

    protected TokenService(JpaRepository<T, Long> tokenRepository, Class<T> type) {
        this.tokenRepository = tokenRepository;
        this.type = type;
    }

    /**
     * Activates the given token by setting its 'isUsed' status to true.
     *
     * @param token the token to be activated
     * @param tokenClass the class of the token
     * @throws TokenIllegalArgumentException if the token is already used or expired
     */
    @Transactional
    public void setTokenActivated(String token) throws TokenIllegalArgumentException {
        T foundToken = findLastToken(token);

        if(foundToken.getIsUsed()){
            throw new TokenIllegalArgumentException("Podany token został już zużyty", this.type);
        }
        if(foundToken.isExpired()){
            throw new TokenIllegalArgumentException("Podany token jest przeterminowany", this.type);
        }
        foundToken.setIsUsed(true);
        tokenRepository.save(foundToken);
    }

    /**
     * Generates a token for a given user and saves it in the database.
     *
     * @param user the user for which the token is generated
     * @return the saved token
     * @throws TokenIllegalArgumentException if the user is null or not a local account
     */
    @Transactional
    public T saveTokenForUser(ApplicationUser user) throws TokenIllegalArgumentException {
        if (user == null) {
            throw new TokenIllegalArgumentException("Nie podano użytkownika", this.type);
        }
        if(!user.getProvider().equals(AuthProvider.LOCAL)){
            throw new TokenIllegalArgumentException("Zmiana hasła niemożliwa", this.type);
        }
        T token = generateTokenForUser(user);
        return tokenRepository.save(token);
    }

    protected abstract T generateTokenForUser(ApplicationUser user);
    protected abstract T findLastToken(String tokenStr);
}
