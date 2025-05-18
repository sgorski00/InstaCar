package pl.studia.InstaCar.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import pl.studia.InstaCar.config.validators.Password;

import java.io.Serializable;

@Getter
@Setter
public class PasswordResetDto implements PasswordEquals, Serializable {

    @Password
    private String password;

    @NotBlank(message = "{NotBlank.confirmPassword}")
    private String confirmPassword;

    public boolean arePasswordsEqual(){
        return password.equals(confirmPassword);
    }
}
