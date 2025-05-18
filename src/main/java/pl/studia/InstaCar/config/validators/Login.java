package pl.studia.InstaCar.config.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(java.lang.annotation.ElementType.FIELD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginValidator.class)
public @interface Login {

    String message() default "{validation.username}";

    Class<?>[] groups() default { };

    Class<? extends java.lang.annotation.ElementType>[] payload() default { };
}
