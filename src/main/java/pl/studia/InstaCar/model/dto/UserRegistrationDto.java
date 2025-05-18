package pl.studia.InstaCar.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import pl.studia.InstaCar.config.validators.Login;
import pl.studia.InstaCar.config.validators.Password;
import pl.studia.InstaCar.model.authentication.ApplicationUser;

import java.io.Serializable;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationDto implements PasswordEquals, Serializable {
    @Login
    private String username;

    @Email(message = "{Email.email}")
    private String email;

    @Password
    private String password;

    @NotBlank(message = "{NotBlank.confirmPassword}")
    private String confirmPassword;

    public boolean arePasswordsEqual(){
        return password != null && password.equals(confirmPassword);
    }

    public ApplicationUser mapToUser(MessageSource messageSource) {
        if(!arePasswordsEqual()) {
            String message = messageSource.getMessage("validation.password.repeat.match", null, LocaleContextHolder.getLocale());
            throw new IllegalArgumentException(message);
        }
        ApplicationUser user = new ApplicationUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
