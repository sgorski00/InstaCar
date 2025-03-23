package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.Vehicle;
import pl.studia.InstaCar.repository.VehicleRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void saveCar(Vehicle car) {
        vehicleRepository.save(car);
    }


    public List<Vehicle> getAllCars() {
        return vehicleRepository.findAll();
    }

    public void saveAllCars(List<Vehicle> cars) {
        vehicleRepository.saveAll(cars);
    }

    public long count() {
        return vehicleRepository.count();
    }

    public Vehicle getCarById(long id) {
        return vehicleRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Car is not found.")
        );
    }
}
