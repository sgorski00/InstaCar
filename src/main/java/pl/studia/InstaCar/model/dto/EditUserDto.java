package pl.studia.InstaCar.model.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.studia.InstaCar.model.UserDetails;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.Role;

import java.io.Serializable;

@Log4j2
@Data
@NoArgsConstructor
public class EditUserDto implements Serializable {

    @NotNull(message = "Należy wybrać rolę")
    private Long roleId;

    @NotBlank(message = "Imię nie może być puste")
    @Size(max = 50, message = "Imię nie może być dłuższe niż 50 znaków")
    private String firstName;

    @NotBlank(message = "Nazwisko nie może być puste")
    @Size(max = 50, message = "Nazwisko nie może być dłuższe niż 50 znaków")
    private String lastName;

    @Nullable
    @NotBlank(message = "Numer telefonu nie może być pusty")
    @Size(max = 50, message = "Numer telefonu nie może być dłuższy niż 50 znaków")
    private String phoneNumber;

    @Nullable
    @NotBlank(message = "Adres nie może być pusty")
    @Size(max = 255, message = "Adres nie może mieć więcej niż 255 znaków")
    private String address;

    @Nullable
    @NotBlank(message = "Miasto nie może być puste")
    @Size(max = 50, message = "Nazwa miasta nie może być dłuższe niż 50 znaków")
    private String city;

    @Nullable
    @NotBlank(message = "Kod pocztowy nie może być pusty")
    @Size(max = 10, message = "Kod pocztowy może mieć maksymalnie 10 znaków")
    private String postalCode;

    public EditUserDto(ApplicationUser user) {
        this.roleId = user.getRole().getId();
        this.firstName = user.getUserDetails() == null ? "" : user.getUserDetails().getFirstName();
        this.lastName = user.getUserDetails() == null ? "" : user.getUserDetails().getLastName();
        this.phoneNumber = user.getUserDetails() == null ? "" : user.getUserDetails().getPhoneNumber();
        this.address = user.getUserDetails() == null ? "" : user.getUserDetails().getAddress();
        this.city = user.getUserDetails() == null ? "" : user.getUserDetails().getCity();
        this.postalCode = user.getUserDetails() == null ? "" : user.getUserDetails().getPostalCode();
    }

    public void updateUser(ApplicationUser user, Role role) {
        user.setRole(role);
        if(user.getUserDetails() == null) {
            UserDetails userDetails = new UserDetails();
            userDetails.setUser(user);
            user.setUserDetails(userDetails);
        }
        user.getUserDetails().setFirstName(firstName);
        user.getUserDetails().setLastName(lastName);
        user.getUserDetails().setPhoneNumber(phoneNumber);
        user.getUserDetails().setAddress(address);
        user.getUserDetails().setCity(city);
        user.getUserDetails().setPostalCode(postalCode);
    }
}
