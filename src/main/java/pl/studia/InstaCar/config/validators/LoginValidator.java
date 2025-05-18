package pl.studia.InstaCar.config.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<Login, String> {

    private static final String LOGIN_REGEX = "^[a-zA-Z0-9]+$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(LOGIN_REGEX) && value.length() >= 5 && value.length() <= 50;
    }
}
