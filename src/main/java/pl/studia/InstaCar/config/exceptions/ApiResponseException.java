package pl.studia.InstaCar.config.exceptions;

import lombok.Getter;

@Getter
public class ApiResponseException extends RuntimeException{

    private final int code;

    public ApiResponseException(String message, int code) {
        super(message);
        this.code = code;
    }
}
