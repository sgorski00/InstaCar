package pl.studia.InstaCar.model.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.studia.InstaCar.model.UserDetails;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.Role;

@Log4j2
@Data
@NoArgsConstructor
public class EditUserDto {

    @NotNull
    private Long roleId;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Nullable
    @Size(max = 50)
    private String phoneNumber;

    @Nullable
    @Size(max = 255)
    private String address;

    @Nullable
    @Size(max = 50)
    private String city;

    @Nullable
    @Size(max = 10)
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
