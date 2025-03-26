package pl.studia.InstaCar.model;

import org.junit.jupiter.api.Test;
import pl.studia.InstaCar.model.enums.RentalStatus;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleTest {

    @Test
    void shouldChangeStatusAfterRent() {
        SportCar car = new SportCar();
        assertEquals(RentalStatus.AVAILABLE, car.getStatus());

        car.rent();

        assertEquals(RentalStatus.RENTED, car.getStatus());
    }

    @Test
    void shouldThrowWhenTryingToRentIfAlreadyRented() {
        SportCar car = new SportCar();
        car.setStatus(RentalStatus.RENTED);

        assertThrows(IllegalStateException.class, car::rent);
    }
    @Test
    void shouldReturnTrueIfAvailable() {
        SportCar car = new SportCar();
        car.setStatus(RentalStatus.AVAILABLE);

        boolean result = car.isAvailable();

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfNotAvailable() {
        SportCar car = new SportCar();
        car.setStatus(RentalStatus.RENTED);

        boolean result = car.isAvailable();

        assertFalse(result);
    }

    @Test
    void shouldChangeStatusAfterReturn() {
        SportCar car = new SportCar();
        car.setStatus(RentalStatus.RENTED);

        car.returnRental();

        assertEquals(RentalStatus.AVAILABLE, car.getStatus());
    }

    @Test
    void shouldThrowWhenTryingToReturnIfNotRented() {
        SportCar car = new SportCar();
        car.setStatus(RentalStatus.AVAILABLE);

        assertThrows(IllegalStateException.class, car::returnRental);
    }
}
