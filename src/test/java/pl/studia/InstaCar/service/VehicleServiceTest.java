package pl.studia.InstaCar.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.model.CityCar;
import pl.studia.InstaCar.model.SportCar;
import pl.studia.InstaCar.model.Vehicle;
import pl.studia.InstaCar.repository.CityCarRepository;
import pl.studia.InstaCar.repository.SportCarRepository;
import pl.studia.InstaCar.repository.VehicleRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private SportCarRepository sportCarRepository;

    @Mock
    private CityCarRepository cityCarRepository;

    @Mock
    private CarModelService carModelService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private VehicleService vehicleService;

    private List<Vehicle> cars;

    @BeforeEach
    public void setUp() {
        CarModel model = new CarModel();
        model.setId(1L);
        CityCar cityCar = new CityCar();
        cityCar.setId(1L);
        cityCar.setModel(model);
        SportCar sportCar = new SportCar();
        sportCar.setModel(model);

        cars = new ArrayList<>();
        cars.add(cityCar);
        cars.add(sportCar);
    }

    @Test
    void shouldReturnAllCars() {
        when(vehicleRepository.findAll()).thenReturn(cars);

        List<Vehicle> result = vehicleService.getAllCars();

        assertEquals(2, result.size());
        assertEquals(CityCar.class, result.get(0).getClass());
        assertEquals(SportCar.class, result.get(1).getClass());

        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    void shouldSaveSportCar() {
        vehicleService.save(cars.get(1));

        verify(carModelService, times(1)).save(any(CarModel.class));
        verify(sportCarRepository, times(1)).save(any(SportCar.class));
    }

    @Test
    void shouldSaveCityCar() {
        vehicleService.save(cars.getFirst());

        verify(carModelService, times(1)).save(any(CarModel.class));
        verify(cityCarRepository, times(1)).save(any(CityCar.class));
    }

    @Test
    void shouldReturnCarById() {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(cars.getFirst()));

        Vehicle result = vehicleService.getCarById(1L);

        assertNotNull(result);
    }

    @Test
    void shouldThrowWhenCarNotFound() {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> vehicleService.getCarById(1L));
        verify(messageSource, times(1)).getMessage(eq("error.car.not.found"), nullable(Object[].class), any(Locale.class));
    }

    @Test
    void shouldDeleteCarByIdButNotModel() {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(cars.getFirst()));
        when(vehicleRepository.countByModel(any(CarModel.class))).thenReturn(1L);

        vehicleService.deleteById(1L);

        verify(vehicleRepository, times(1)).deleteById(anyLong());
        verify(carModelService, times(0)).deleteById(anyLong());
    }

    @Test
    void shouldDeleteCarAndModelById() {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(cars.getFirst()));
        when(vehicleRepository.countByModel(any(CarModel.class))).thenReturn(0L);

        vehicleService.deleteById(1L);

        verify(vehicleRepository, times(1)).deleteById(anyLong());
        verify(carModelService, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldThrowWhenVehicleNotFoundWhileDeleting() {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> vehicleService.deleteById(1L));
        verify(messageSource, times(1)).getMessage(eq("error.car.not.found"), nullable(Object[].class), any(Locale.class));
    }
}
