package pl.studia.InstaCar.service.tokens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.event.PasswordResetEvent;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.PasswordResetToken;
import pl.studia.InstaCar.repository.PasswordTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordTokenService extends TokenService<PasswordResetToken> {

    @Autowired
    public PasswordTokenService(PasswordTokenRepository passwordTokenRepository) {
        super(passwordTokenRepository);
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

    public ApplicationUser getUserForToken(String tokenStr) {
        Optional<PasswordResetToken> oToken = tokenRepository.findAll().stream()
                .filter(t -> t.getToken().equals(tokenStr))
                .findFirst();
        if(oToken.isPresent()) {
            PasswordResetToken token = oToken.get();
            if (token.getIsUsed()) {
                throw new IllegalArgumentException("Podany token został wykorzystany");
            }
            return token.getUser();
        } else {
            throw new IllegalArgumentException("Podany token nie istnieje");
        }
    }
}
