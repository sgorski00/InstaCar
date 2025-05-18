package pl.studia.InstaCar.service.tokens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.config.exceptions.TokenIllegalArgumentException;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.PasswordResetToken;
import pl.studia.InstaCar.repository.PasswordTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordTokenService extends TokenService<PasswordResetToken> {

    private final PasswordTokenRepository passwordTokenRepository;
    private final MessageSource messageSource;

    @Autowired
    public PasswordTokenService(PasswordTokenRepository passwordTokenRepository, @Qualifier("messageSource") MessageSource messageSource) {
        super(messageSource, passwordTokenRepository, PasswordResetToken.class);
        this.passwordTokenRepository = passwordTokenRepository;
        this.messageSource = messageSource;
    }

    @Override
    protected PasswordResetToken generateTokenForUser(ApplicationUser user) {
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setIsUsed(false);
        token.setExpiresAt(LocalDateTime.now().plusHours(24));
        return token;
    }

    @Override
    protected PasswordResetToken findLastToken(String tokenStr) {
        return passwordTokenRepository.findFirstByTokenOrderByIdDesc(tokenStr)
                .orElseThrow(() -> {
                    String message = messageSource.getMessage("error.token.not.found", null, LocaleContextHolder.getLocale());
                    return new TokenIllegalArgumentException(message, PasswordResetToken.class);
                });
    }

    /**
     * Retrieves the user associated with the given token.
     *
     * @param tokenStr the token string to find the user for
     * @return the user associated with the token
     * @throws TokenIllegalArgumentException if the token is already used or does not exist
     */
    public ApplicationUser getUserForToken(String tokenStr) {
        Optional<PasswordResetToken> oToken = tokenRepository.findAll().stream()
                .filter(t -> t.getToken().equals(tokenStr))
                .findFirst();
        if(oToken.isPresent()) {
            PasswordResetToken token = oToken.get();
            if (token.getIsUsed()) {
                String message = messageSource.getMessage("error.token.used", null, LocaleContextHolder.getLocale());
                throw new TokenIllegalArgumentException(message, PasswordResetToken.class);
            }
            return token.getUser();
        } else {
            String message = messageSource.getMessage("error.token.not.found", null, LocaleContextHolder.getLocale());
            throw new TokenIllegalArgumentException(message, PasswordResetToken.class);
        }
    }
}
