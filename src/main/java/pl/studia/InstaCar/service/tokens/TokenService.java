package pl.studia.InstaCar.service.tokens;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.InstaCar.config.exceptions.TokenIllegalArgumentException;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.AuthProvider;
import pl.studia.InstaCar.model.authentication.tokens.BaseToken;

public abstract class TokenService<T extends BaseToken> {

    private final MessageSource messageSource;
    private final Class<T> type;
    protected final JpaRepository<T, Long> tokenRepository;

    protected TokenService(MessageSource messageSource, JpaRepository<T, Long> tokenRepository, Class<T> type) {
        this.messageSource = messageSource;
        this.tokenRepository = tokenRepository;
        this.type = type;
    }

    /**
     * Activates the given token by setting its 'isUsed' status to true.
     *
     * @param token the token to be activated
     * @throws TokenIllegalArgumentException if the token is already used or expired
     */
    @Transactional
    public void setTokenActivated(String token) throws TokenIllegalArgumentException {
        T foundToken = findLastToken(token);

        if(foundToken.getIsUsed()){
            String message = messageSource.getMessage("error.token.used", null, LocaleContextHolder.getLocale());
            throw new TokenIllegalArgumentException(message, this.type);
        }
        if(foundToken.isExpired()){
            String message = messageSource.getMessage("error.token.expired", null, LocaleContextHolder.getLocale());
            throw new TokenIllegalArgumentException(message, this.type);
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
            String message = messageSource.getMessage("error.token.user.null", null, LocaleContextHolder.getLocale());
            throw new TokenIllegalArgumentException(message, this.type);
        }
        if(!user.getProvider().equals(AuthProvider.LOCAL)){
            String message = messageSource.getMessage("error.token.user.not.local", null, LocaleContextHolder.getLocale());
            throw new TokenIllegalArgumentException(message, this.type);
        }
        T token = generateTokenForUser(user);
        return tokenRepository.save(token);
    }

    protected abstract T generateTokenForUser(ApplicationUser user);
    protected abstract T findLastToken(String tokenStr);
}
