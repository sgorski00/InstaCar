package pl.studia.InstaCar.model.dto;

import lombok.Data;
import pl.studia.InstaCar.model.Rent;
import pl.studia.InstaCar.model.UserDetails;
import pl.studia.InstaCar.model.Vehicle;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.service.CityService;

import java.time.LocalDate;

@Data
public class OrderDto {

    private Vehicle car;
    private UserDetails userDetails;
    private LocalDate pickUpDate;
    private LocalDate returnDate;
    private Long pickUpCityId;
    private Long returnCityId;
    private String additionalInfo;

    public OrderDto(UserDetails userDetails, Vehicle car, LocalDate pickUpDate, LocalDate returnDate) {
        this.car = car;
        this.userDetails = userDetails;
        this.pickUpDate = pickUpDate;
        this.returnDate = returnDate;
    }

    public double getTotalPrice() {
        return car.getPrice() * (returnDate.toEpochDay() - pickUpDate.toEpochDay() + 1);
    }
}
