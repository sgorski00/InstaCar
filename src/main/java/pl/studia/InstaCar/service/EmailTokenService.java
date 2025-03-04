package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.EmailToken;
import pl.studia.InstaCar.repository.EmailTokenRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class EmailTokenService {

    private final EmailTokenRepository emailTokenRepository;

    @Autowired
    public EmailTokenService(EmailTokenRepository emailTokenRepository) {
        this.emailTokenRepository = emailTokenRepository;
    }

    public void setTokenActivated(String token) {
        EmailToken emailToken = emailTokenRepository.findFirstByTokenOrderByIdDesc(token).orElseThrow(
                () -> new NoSuchElementException("Podany token nie istnieje")
        );

        emailToken.setIsVerified(true);
        emailTokenRepository.save(emailToken);
    }

    public EmailToken saveTokenForUser(ApplicationUser user) {
        EmailToken token = generateTokenForUser(user);
        return emailTokenRepository.save(token);
    }

    private EmailToken generateTokenForUser(ApplicationUser user) {
        return EmailToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .isVerified(false)
                .build();
    }
}
