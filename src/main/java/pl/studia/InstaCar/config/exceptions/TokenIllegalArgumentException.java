package pl.studia.InstaCar.config.exceptions;

import lombok.Getter;
import pl.studia.InstaCar.model.authentication.tokens.BaseToken;

@Getter
public class TokenIllegalArgumentException extends IllegalArgumentException{

    private final Class<? extends BaseToken> tokenClass;

    public TokenIllegalArgumentException(String message, Class<? extends BaseToken> tokenClass) {
        super(message);
        this.tokenClass = tokenClass;
    }
}