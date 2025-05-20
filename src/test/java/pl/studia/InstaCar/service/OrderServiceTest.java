package pl.studia.InstaCar.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import pl.studia.InstaCar.model.City;
import pl.studia.InstaCar.model.Rent;
import pl.studia.InstaCar.model.SportCar;
import pl.studia.InstaCar.model.UserDetails;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.dto.OrderDto;
import pl.studia.InstaCar.model.enums.RentStatus;
import pl.studia.InstaCar.repository.RentRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private RentRepository rentRepository;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private CityService cityService;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private OrderService orderService;

    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        UserDetails userDetails = new UserDetails();
        SportCar sportCar = new SportCar();
        orderDto = new OrderDto(userDetails, sportCar,
                LocalDate.now().minusDays(7),
                LocalDate.now().plusDays(7)
        );
        orderDto.setPickUpCityId(1L);
        orderDto.setReturnCityId(1L);
    }

    @Test
    void shouldCreateOrder() {
        ArgumentCaptor<Rent> rentCaptor = ArgumentCaptor.forClass(Rent.class);
        ApplicationUser user = new ApplicationUser();
        City city = new City();
        when(cityService.getCityById(anyLong())).thenReturn(city);
        when(rentRepository.save(any(Rent.class))).thenReturn(new Rent());

        orderService.createOrder(orderDto, user);

        verify(rentRepository, times(1)).save(rentCaptor.capture());
        verify(eventPublisher, times(1)).publishEvent(any());
        Rent result = rentCaptor.getValue();
        assertEquals(city, result.getPickUpCity());
        assertEquals(city, result.getReturnCity());
        assertEquals(user, result.getUser());
        assertEquals(RentStatus.PENDING, result.getRentStatus());
        assertEquals(orderDto.getPickUpDate(), result.getRentDate());
        assertEquals(orderDto.getReturnDate(), result.getReturnDate());
        assertEquals(orderDto.getCar(), result.getVehicle());
    }

    @Test
    void shouldThrowWhenOrderNotFoundById() {
        when(rentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderService.getOrderById(1L));
        verify(messageSource, times(1)).getMessage(eq("error.rent.not.found"), nullable(Object[].class), any(Locale.class));
    }

    @Test
    void shouldCancelOrderById() {
        Rent rent = new Rent();
        rent.setRentStatus(RentStatus.PENDING, messageSource);
        when(rentRepository.findById(anyLong())).thenReturn(Optional.of(rent));

        orderService.cancelOrderById(1L);

        assertEquals(RentStatus.CANCELLED, rent.getRentStatus());
        verify(rentRepository, times(1)).save(any(Rent.class));
    }

    @Test
    void shouldAcceptOrderById() {
        Rent rent = new Rent();
        rent.setRentStatus(RentStatus.PENDING, messageSource);
        when(rentRepository.findById(anyLong())).thenReturn(Optional.of(rent));

        orderService.acceptOrderById(1L);

        assertEquals(RentStatus.ACTIVE, rent.getRentStatus());
        verify(rentRepository, times(1)).save(any(Rent.class));
    }

    @Test
    void shouldCancelExpiredOrdersOlderThan() {
        Rent oldRent1 = new Rent();
        oldRent1.setRentStatus(RentStatus.PENDING, messageSource);
        oldRent1.setCreatedAt(Timestamp.from(Instant.now().minus(3, ChronoUnit.DAYS)));
        Rent oldRent2 = new Rent();
        oldRent2.setRentStatus(RentStatus.PENDING, messageSource);
        oldRent2.setCreatedAt(Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS)));
        List<Rent> rents = new ArrayList<>(Arrays.asList(oldRent1, oldRent2));

        when(rentRepository.findAllByCreatedAtBefore(any(LocalDate.class))).thenReturn(rents);

        orderService.cancelExpiredOrdersOlderThan(LocalDate.now().minusDays(1));

        assertEquals(RentStatus.CANCELLED, oldRent1.getRentStatus());
        assertEquals(RentStatus.CANCELLED, oldRent2.getRentStatus());
        verify(rentRepository, times(1)).findAllByCreatedAtBefore(any(LocalDate.class));
        verify(rentRepository, times(2)).save(any(Rent.class));
    }

}
