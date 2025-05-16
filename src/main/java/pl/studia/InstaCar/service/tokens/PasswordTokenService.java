package pl.studia.InstaCar.service.tokens;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public PasswordTokenService(PasswordTokenRepository passwordTokenRepository) {
        super(passwordTokenRepository, PasswordResetToken.class);
        this.passwordTokenRepository = passwordTokenRepository;
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
                .orElseThrow(() -> new TokenIllegalArgumentException("Podany token nie istnieje", PasswordResetToken.class));
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
                throw new TokenIllegalArgumentException("Podany token został wykorzystany", PasswordResetToken.class);
            }
            return token.getUser();
        } else {
            throw new TokenIllegalArgumentException("Podany token nie istnieje", PasswordResetToken.class);
        }
    }
}
