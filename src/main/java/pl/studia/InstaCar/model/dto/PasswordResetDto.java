package pl.studia.InstaCar.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.studia.InstaCar.config.validators.Password;
import pl.studia.InstaCar.model.authentication.ApplicationUser;

import java.io.Serializable;

@Getter
@Setter
public class PasswordResetDto implements PasswordEquals, Serializable {

    @Password
    private String password;

    @NotBlank(message = "{NotBlank.confirmPassword}")
    private String confirmPassword;

    @NotBlank(message = "{NotBlank.token}")
    private String token;

    public PasswordResetDto(String token) {
        this.token = token;
    }

    public boolean arePasswordsEqual(){
        return password.equals(confirmPassword);
    }
}
