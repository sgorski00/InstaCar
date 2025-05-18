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

    @NotNull(message = "{NotNull.role}")
    private Long roleId;

    @NotBlank(message = "{NotBlank.firstName}")
    @Size(max = 50, message = "{Size.firstName}")
    private String firstName;

    @NotBlank(message = "{NotBlank.lastName}")
    @Size(max = 50, message = "{Size.lastName}")
    private String lastName;

    @Nullable
    @NotBlank(message = "{NotBlank.phoneNumber}")
    @Size(max = 50, message = "{Size.phoneNumber}")
    private String phoneNumber;

    @Nullable
    @NotBlank(message = "{NotBlank.address}")
    @Size(max = 255, message = "{Size.address}")
    private String address;

    @Nullable
    @NotBlank(message = "{NotBlank.city}")
    @Size(max = 50, message = "{Size.city}")
    private String city;

    @Nullable
    @NotBlank(message = "{NotBlank.postalCode}")
    @Size(max = 10, message = "{Size.postalCode}")
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
