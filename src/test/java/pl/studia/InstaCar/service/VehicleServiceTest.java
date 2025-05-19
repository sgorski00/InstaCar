package pl.studia.InstaCar.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.studia.InstaCar.model.CityCar;
import pl.studia.InstaCar.model.SportCar;
import pl.studia.InstaCar.model.Vehicle;
import pl.studia.InstaCar.repository.CityCarRepository;
import pl.studia.InstaCar.repository.SportCarRepository;
import pl.studia.InstaCar.repository.VehicleRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @InjectMocks
    private VehicleService vehicleService;

    private List<Vehicle> cars;

    @BeforeEach
    public void setUp() {
        cars = new ArrayList<>();
        cars.add(new CityCar());
        cars.add(new SportCar());
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
        vehicleService.save(new SportCar());

        verify(sportCarRepository, times(1)).save(any(SportCar.class));
    }

    @Test
    void shouldSaveCityCar() {
        vehicleService.save(new CityCar());

        verify(cityCarRepository, times(1)).save(any(CityCar.class));
    }
}
