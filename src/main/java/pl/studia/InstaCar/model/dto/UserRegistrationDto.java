package pl.studia.InstaCar.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.studia.InstaCar.config.exceptions.EntityValidationException;
import pl.studia.InstaCar.model.authentication.ApplicationUser;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationDto implements PasswordEquals{
    @NotBlank
    @Size(min = 5, max = 50)
    private String username;
    @Email
    private String email;

    @NotBlank
    @Size(min = 5, max = 50)
    private String password;

    @NotBlank
    private String confirmPassword;

    public boolean arePasswordsEqual(){
        return password != null && password.equals(confirmPassword);
    }

    public ApplicationUser mapToUser() {
        if(!arePasswordsEqual()) {
            throw new EntityValidationException("Podane hasła nie są takie same", "redirect:/register");
        }
        ApplicationUser user = new ApplicationUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
