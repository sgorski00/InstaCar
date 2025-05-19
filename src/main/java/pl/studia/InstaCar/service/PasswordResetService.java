package pl.studia.InstaCar.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.InstaCar.config.exceptions.TokenIllegalArgumentException;
import pl.studia.InstaCar.service.event.PasswordResetEvent;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.tokens.PasswordResetToken;
import pl.studia.InstaCar.model.dto.PasswordResetDto;
import pl.studia.InstaCar.service.tokens.PasswordTokenService;

@Log4j2
@Service
public class PasswordResetService {

    private final ApplicationEventPublisher eventPublisher;
    private final PasswordTokenService passwordTokenService;
    private final UserService userService;

    @Autowired
    public PasswordResetService(ApplicationEventPublisher eventPublisher, PasswordTokenService passwordTokenService, UserService userService) {
        this.eventPublisher = eventPublisher;
        this.passwordTokenService = passwordTokenService;
        this.userService = userService;
    }

    /**
     * Generates password reset token for given user and publishes {@link PasswordResetEvent}.
     *
     * @param user user to generate token for
     * @throws TokenIllegalArgumentException if an error occurs during token generation
     */
    @Transactional
    public void generateToken(ApplicationUser user) {
        try {
            PasswordResetToken token = passwordTokenService.saveTokenForUser(user);
            log.info("token: {}", token.getToken());
            log.info("user: {}", token.getUser());
            eventPublisher.publishEvent(new PasswordResetEvent(this, user, token));
        }catch (Exception e) {
            log.error("An error occured in generateToken method: {}", e.getCause().toString());
            throw new TokenIllegalArgumentException(e.getMessage(), PasswordResetToken.class);
        }
    }

    /**
     * Changes user's password and marks token as used.
     *
     * @param user user to change password for
     * @param resetDto password reset data
     */
    @Transactional
    public void changePassword(ApplicationUser user, PasswordResetDto resetDto) throws TokenIllegalArgumentException {
        try {
            user.setPassword(resetDto.getPassword());
            userService.save(user);
            passwordTokenService.setTokenActivated(resetDto.getToken());
        } catch (TokenIllegalArgumentException e) {
            throw new TokenIllegalArgumentException(e.getMessage(), PasswordResetToken.class);
        }
    }
}
