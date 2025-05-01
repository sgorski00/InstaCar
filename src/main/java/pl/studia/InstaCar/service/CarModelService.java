package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.repository.CarModelRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CarModelService {
    private final CarModelRepository carModelRepository;

    @Autowired
    public CarModelService(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    public long count() {
        return carModelRepository.count();
    }

    @CacheEvict(value = "allCarModels", allEntries = true)
    public void saveAll(Iterable<CarModel> models) {
        carModelRepository.saveAll(models);
    }

    @Cacheable(value = "allCarModels")
    public List<CarModel> getAllCarModels() {
        return carModelRepository.findAll();
    }

    @Cacheable(value = "carModels", key = "#id")
    public CarModel getCarModelById(Long id) {
        return carModelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Car model not found with id: " + id));
    }

    @Caching(
            put = @CachePut(value = "carModels", key = "#result.id"),
            evict = @CacheEvict(value = "allCarModels", allEntries = true)
    )
    public CarModel save(CarModel carModel) {
        return carModelRepository.save(carModel);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "carModels", key = "#id"),
                    @CacheEvict(value = "allCarModels", allEntries = true)
            }
    )
    public void deleteById(Long id) {
        carModelRepository.deleteById(id);
    }
}
