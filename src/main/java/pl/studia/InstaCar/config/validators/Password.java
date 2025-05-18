package pl.studia.InstaCar.config.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {

    String message() default "{validation.password}";

    Class<?>[] groups() default { };

    Class<? extends java.lang.annotation.ElementType>[] payload() default { };
}
