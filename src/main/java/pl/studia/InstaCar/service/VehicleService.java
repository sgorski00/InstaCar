package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Cacheable(value = "cars", key = "#id")
    public Vehicle getCarById(long id) {
        return vehicleRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Car is not found.")
        );
    }

    @Caching(
            put = @CachePut(value = "cars", key = "#result.id"),
            evict = @CacheEvict(value = "allCars", allEntries = true)
    )
    public Vehicle save(Vehicle vehicle) {
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
    public void deleteById(long id) {
        vehicleRepository.deleteById(id);
    }

    public List<Vehicle> getAllCarsByQuery(String query) {
        return vehicleRepository.findAllByQuery(query.toLowerCase());
    }
}
