package pl.studia.InstaCar.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PasswordResetDto implements PasswordEquals, Serializable {

    @NotBlank(message = "Hasło jest wymagane")
    @Size(min = 5, message = "Hasło musi mieć co najmniej 5 znaków")
    private String password;

    @NotBlank(message = "Hasło jest wymagane")
    private String repeatPassword;

    public boolean arePasswordsEqual(){
        return password.equals(repeatPassword);
    }
}
