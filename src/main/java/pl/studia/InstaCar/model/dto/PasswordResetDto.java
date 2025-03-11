package pl.studia.InstaCar.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetDto {

    @NotBlank
    @Size(min = 5)
    private String password;

    @NotBlank
    private String repeatPassword;

    public boolean arePasswordsEqual(){
        return password.equals(repeatPassword);
    }
}
