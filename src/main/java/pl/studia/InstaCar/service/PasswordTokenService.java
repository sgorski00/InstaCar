package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.PasswordResetToken;
import pl.studia.InstaCar.repository.PasswordTokenRepository;

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
        return token;
    }
}
