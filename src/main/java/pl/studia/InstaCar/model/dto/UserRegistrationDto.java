package pl.studia.InstaCar.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.studia.InstaCar.model.authentication.ApplicationUser;

import java.io.Serializable;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationDto implements PasswordEquals, Serializable {
    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    @Size(min = 5, max = 50, message = "Nazwa użytkownika musi mieć od 5 do 50 znaków")
    private String username;

    @Email(message = "Podany email jest niepoprawny")
    private String email;

    @NotBlank(message = "Hasło jest wymagane")
    @Size(min = 5, max = 50, message = "Hasło musi mieć od 5 do 50 znaków")
    private String password;

    @NotBlank(message = "Należy podać powtórzone hasło")
    private String confirmPassword;

    public boolean arePasswordsEqual(){
        return password != null && password.equals(confirmPassword);
    }

    public ApplicationUser mapToUser() {
        if(!arePasswordsEqual()) {
            throw new IllegalArgumentException("Podane hasła nie są takie same");
        }
        ApplicationUser user = new ApplicationUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
