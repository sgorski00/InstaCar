package pl.studia.InstaCar.service.tokens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.EmailActivationToken;
import pl.studia.InstaCar.repository.EmailTokenRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class EmailTokenService extends TokenService<EmailActivationToken> {

    private final EmailTokenRepository emailTokenRepository;
    private final MessageSource messageSource;

    @Autowired
    public EmailTokenService(EmailTokenRepository emailTokenRepository, MessageSource messageSource) {
        super(messageSource, emailTokenRepository, EmailActivationToken.class);
        this.emailTokenRepository = emailTokenRepository;
        this.messageSource = messageSource;
    }

    @Override
    protected EmailActivationToken generateTokenForUser(ApplicationUser user) {
        EmailActivationToken token = new EmailActivationToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setIsUsed(false);
        token.setExpiresAt(LocalDateTime.now().plusHours(24));
        return token;
    }

    @Override
    protected EmailActivationToken findLastToken(String tokenStr) {
        String message = messageSource.getMessage("error.token.not.found", null, LocaleContextHolder.getLocale());
        return emailTokenRepository.findFirstByTokenOrderByIdDesc(tokenStr)
                .orElseThrow(() -> new NoSuchElementException(message));
    }
}
