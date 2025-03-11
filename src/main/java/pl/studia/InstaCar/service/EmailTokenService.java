package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.EmailActivationToken;
import pl.studia.InstaCar.repository.EmailTokenRepository;

import java.util.UUID;

@Service
public class EmailTokenService extends TokenService<EmailActivationToken> {

    @Autowired
    public EmailTokenService(EmailTokenRepository emailTokenRepository) {
        super(emailTokenRepository);
    }

    @Override
    protected EmailActivationToken generateTokenForUser(ApplicationUser user) {
        EmailActivationToken token = new EmailActivationToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setIsUsed(false);
        return token;
    }
}
