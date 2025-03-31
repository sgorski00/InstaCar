package pl.studia.InstaCar.config.exceptions;

import lombok.Getter;

@Getter
public class EntityValidationException extends RuntimeException {
    private final String redirectUrl;

    public EntityValidationException(String message, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
    }
}
