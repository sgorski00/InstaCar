package pl.studia.InstaCar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import pl.studia.InstaCar.config.exceptions.NotAvailableException;
import pl.studia.InstaCar.model.enums.RentStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class VehicleTest {

    private MessageSource messageSource;
    private Vehicle vehicle;
    private List<Rent> rents;

    @BeforeEach
    void setUp() {
        vehicle = new CityCar();
        rents = new ArrayList<>();
        vehicle.setRents(rents);
        messageSource = mock(MessageSource.class);
    }

    @Test
    void shouldReturnTrueIfRentListIsEmpty() {
        boolean result = vehicle.isAvailable(LocalDate.now(), LocalDate.now().plusDays(1));

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueIfRentListIsNull() {
        vehicle.setRents(null);

        boolean result = vehicle.isAvailable(LocalDate.now(), LocalDate.now().plusDays(1));

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueIfThereIsNoRentInGivenPeriodStartBetweenTwoRents() {
        Rent rent1 = new Rent();
        rent1.setRentStatus(RentStatus.PENDING, messageSource);
        rent1.setRentDate(LocalDate.now().minusDays(1));
        rent1.setReturnDate(LocalDate.now().plusDays(1));
        Rent rent2 = new Rent();
        rent2.setRentStatus(RentStatus.PENDING, messageSource);
        rent2.setRentDate(LocalDate.now().plusDays(6));
        rent2.setReturnDate(LocalDate.now().plusDays(10));
        rents.add(rent1);
        rents.add(rent2);

        boolean result = vehicle.isAvailable(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5));

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueIfThereIsNoRentInGivenPeriodStartAfterPreviousRent() {
        Rent rent1 = new Rent();
        rent1.setRentStatus(RentStatus.PENDING, messageSource);
        rent1.setRentDate(LocalDate.now().minusDays(1));
        rent1.setReturnDate(LocalDate.now().plusDays(1));
        rents.add(rent1);

        boolean result = vehicle.isAvailable(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5));

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueIfThereIsNoRentInGivenPeriodStartBeforeNextRent() {
        Rent rent1 = new Rent();
        rent1.setRentStatus(RentStatus.PENDING, messageSource);
        rent1.setRentDate(LocalDate.now().plusDays(5));
        rent1.setReturnDate(LocalDate.now().plusDays(10));
        rents.add(rent1);

        boolean result = vehicle.isAvailable(LocalDate.now(), LocalDate.now().plusDays(4));

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfThereIsRentInGivenPeriodStartInsidePeriod() {
        Rent rent1 = new Rent();
        rent1.setRentStatus(RentStatus.PENDING, messageSource);
        rent1.setRentDate(LocalDate.now().minusDays(1));
        rent1.setReturnDate(LocalDate.now().plusDays(1));
        rents.add(rent1);

        boolean result = vehicle.isAvailable(LocalDate.now(), LocalDate.now().plusDays(1));

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseIfThereIsRentInGivenPeriodStartSameDay() {
        Rent rent1 = new Rent();
        rent1.setRentStatus(RentStatus.PENDING, messageSource);
        rent1.setRentDate(LocalDate.now().minusDays(1));
        rent1.setReturnDate(LocalDate.now().plusDays(1));
        rents.add(rent1);

        boolean result = vehicle.isAvailable(LocalDate.now().minusDays(1), LocalDate.now().plusDays(5));

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseIfThereIsRentInGivenPeriodEndSameDay() {
        Rent rent1 = new Rent();
        rent1.setRentStatus(RentStatus.PENDING, messageSource);
        rent1.setRentDate(LocalDate.now().minusDays(1));
        rent1.setReturnDate(LocalDate.now().plusDays(1));
        rents.add(rent1);

        boolean result = vehicle.isAvailable(LocalDate.now().minusDays(5), LocalDate.now().plusDays(1));

        assertFalse(result);
    }

    @Test
    void shouldReturnPathToEmptyImageIfThereIsNoImage() {
        String result = vehicle.getImageUrl();

        assertEquals("/images/no_image.png", result);
    }

    @Test
    void shouldReturnPathToImageIfThereIsImage() {
        String path = "/path/to/image.png";
        vehicle.setImageUrl(path);

        String result = vehicle.getImageUrl();

        assertEquals(path, result);
    }

    @Test
    void shouldAddRentIfAvailable() {
        Rent rent = new Rent();
        vehicle.rent(rent, messageSource);

        assertEquals(1, vehicle.getRents().size());
        assertEquals(rent, vehicle.getRents().getFirst());
    }

    @Test
    void shouldNotAddRentIfNotAvailable() {
        Rent rent1 = new Rent();
        rent1.setRentStatus(RentStatus.PENDING, messageSource);
        rent1.setRentDate(LocalDate.now().minusDays(1));
        rent1.setReturnDate(LocalDate.now().plusDays(1));
        rents.add(rent1);
        Rent rentToAdd = new Rent();
        rentToAdd.setRentDate(LocalDate.now());
        rentToAdd.setReturnDate(LocalDate.now().plusDays(5));

        assertThrows(NotAvailableException.class, () -> vehicle.rent(rentToAdd, messageSource));

        assertEquals(1, vehicle.getRents().size());
        verify(messageSource, times(1)).getMessage(eq("error.rent.not.available"), nullable(Object[].class), any(Locale.class));
    }
}
