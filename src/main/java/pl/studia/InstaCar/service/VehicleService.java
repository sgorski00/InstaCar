package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.CityCar;
import pl.studia.InstaCar.model.SportCar;
import pl.studia.InstaCar.model.Vehicle;
import pl.studia.InstaCar.repository.CityCarRepository;
import pl.studia.InstaCar.repository.SportCarRepository;
import pl.studia.InstaCar.repository.VehicleRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final SportCarRepository sportCarRepository;
    private final CityCarRepository cityCarRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, SportCarRepository sportCarRepository, CityCarRepository cityCarRepository) {
        this.vehicleRepository = vehicleRepository;
        this.sportCarRepository = sportCarRepository;
        this.cityCarRepository = cityCarRepository;
    }

    public List<Vehicle> getAllCars() {
        return vehicleRepository.findAll();
    }

    public long count() {
        return vehicleRepository.count();
    }

    public Vehicle getCarById(long id) {
        return vehicleRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Car is not found.")
        );
    }

    public void save(Vehicle vehicle) {
        if(vehicle instanceof CityCar cityCar) {
            cityCarRepository.save(cityCar);
        } else if (vehicle instanceof SportCar sportCar) {
            sportCarRepository.save(sportCar);
        } else {
            throw new IllegalArgumentException("Vehicle type is not supported");
        }
    }
}
