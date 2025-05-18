package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.repository.CarModelRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CarModelService {
    private final CarModelRepository carModelRepository;
    private final MessageSource messageSource;

    @Autowired
    public CarModelService(CarModelRepository carModelRepository, @Qualifier("messageSource") MessageSource messageSource) {
        this.carModelRepository = carModelRepository;
        this.messageSource = messageSource;
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
        String message = messageSource.getMessage("error.car.model.not.found", null, LocaleContextHolder.getLocale());
        return carModelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(message +": " + id));
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
