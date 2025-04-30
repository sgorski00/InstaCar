package pl.studia.InstaCar.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.model.CityCar;
import pl.studia.InstaCar.model.SportCar;
import pl.studia.InstaCar.model.Vehicle;
import pl.studia.InstaCar.repository.CityCarRepository;
import pl.studia.InstaCar.repository.SportCarRepository;
import pl.studia.InstaCar.repository.VehicleRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Log4j2
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final SportCarRepository sportCarRepository;
    private final CityCarRepository cityCarRepository;
    private final CarModelService carModelService;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, SportCarRepository sportCarRepository, CityCarRepository cityCarRepository, CarModelService carModelService) {
        this.vehicleRepository = vehicleRepository;
        this.sportCarRepository = sportCarRepository;
        this.cityCarRepository = cityCarRepository;
        this.carModelService = carModelService;
    }

    public List<Vehicle> getAllCars() {
        return vehicleRepository.findAll();
    }

    public long count() {
        return vehicleRepository.count();
    }

    @Cacheable(value = "cars", key = "#id")
    public Vehicle getCarById(long id) {
        return vehicleRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Car with id: " + id + " is not found.")
        );
    }

    @Caching(
            put = @CachePut(value = "cars", key = "#result.id"),
            evict = @CacheEvict(value = "allCars", allEntries = true)

    )
    @Transactional
    public Vehicle save(Vehicle vehicle) {
        carModelService.save(vehicle.getModel());
        if (vehicle instanceof CityCar cityCar) {
            return cityCarRepository.save(cityCar);
        } else if (vehicle instanceof SportCar sportCar) {
            return sportCarRepository.save(sportCar);
        } else {
            throw new IllegalArgumentException("Vehicle type is not supported");
        }
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "cars", key = "#id"),
                    @CacheEvict(value = "allCars", allEntries = true)
            }
    )
    @Transactional
    public void deleteById(long id) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Car with id: " + id + " is not found.")
        );
        CarModel carModel = vehicle.getModel();
        vehicle.setModel(null);
        vehicleRepository.deleteById(vehicle.getId());
        boolean modelUnused = vehicleRepository.countByModel(carModel) == 0;
        log.info("modelUnused: {}", modelUnused);
        log.info("carModel: {}", carModel);
        if(modelUnused) {
            carModelService.deleteById(carModel.getId());
        }
    }

    public List<Vehicle> getAllCarsByQuery(String query) {
        return vehicleRepository.findAllByQuery(query.toLowerCase());
    }

    public List<Vehicle> getAllCarsByQueryAndType(String query, String type) {
        query = query == null ?  "" : query;
        return vehicleRepository.findAllByQueryAndType(query.toLowerCase(), type.toLowerCase());
    }
}
