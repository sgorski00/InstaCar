package pl.studia.InstaCar.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.model.CityCar;
import pl.studia.InstaCar.model.SportCar;
import pl.studia.InstaCar.model.Vehicle;
import pl.studia.InstaCar.model.dto.NewCarDto;
import pl.studia.InstaCar.repository.CityCarRepository;
import pl.studia.InstaCar.repository.SportCarRepository;
import pl.studia.InstaCar.repository.VehicleRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Log4j2
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final SportCarRepository sportCarRepository;
    private final CityCarRepository cityCarRepository;
    private final CarModelService carModelService;
    private final FileUploadService fileUploadService;
    private final Validator validator;
    private final MessageSource messageSource;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, SportCarRepository sportCarRepository, CityCarRepository cityCarRepository, CarModelService carModelService, FileUploadService fileUploadService, Validator validator, @Qualifier("messageSource") MessageSource messageSource) {
        this.vehicleRepository = vehicleRepository;
        this.sportCarRepository = sportCarRepository;
        this.cityCarRepository = cityCarRepository;
        this.carModelService = carModelService;
        this.fileUploadService = fileUploadService;
        this.validator = validator;
        this.messageSource = messageSource;
    }

    @Cacheable(value = "allCars")
    public List<Vehicle> getAllCars() {
        return vehicleRepository.findAll();
    }

    public long count() {
        return vehicleRepository.count();
    }

    @Cacheable(value = "cars", key = "#id")
    public Vehicle getCarById(long id) {
        return vehicleRepository.findById(id).orElseThrow(
                () -> {
                    String message = messageSource.getMessage("error.car.not.found", null, LocaleContextHolder.getLocale());
                    return new NoSuchElementException(message + ": " + id);
                }
        );
    }

    public Vehicle getCarByIdWithRents(long id) {
        return vehicleRepository.findByIdWithRents(id).orElseThrow(
                () -> {
                    String message = messageSource.getMessage("error.car.not.found", null, LocaleContextHolder.getLocale());
                    return new NoSuchElementException(message + ": " + id);
                }
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
            String message = messageSource.getMessage("error.vehicle.type.not.supported", null, LocaleContextHolder.getLocale());
            throw new IllegalArgumentException(message);
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
                () -> {
                    String message = messageSource.getMessage("error.car.not.found", null, LocaleContextHolder.getLocale());
                    return new NoSuchElementException(message + ": " + id);
                }
        );
        CarModel carModel = vehicle.getModel();
        vehicle.setModel(null);
        vehicleRepository.deleteById(vehicle.getId());
        boolean modelUnused = vehicleRepository.countByModel(carModel) == 0;
        log.debug("modelUnused: {}", modelUnused);
        log.debug("carModel: {}", carModel);
        if(modelUnused) {
            carModelService.deleteById(carModel.getId());
        }
    }

    public List<Vehicle> getAllCarsByQuery(String query) {
        return vehicleRepository.findAllByQuery(query.toLowerCase());
    }

    public List<Vehicle> getAllCarsByQueryAndTypeAvailableInDate(String query, String type, LocalDate dateFrom, LocalDate dateTo) {
        query = query == null ?  "" : query;
        List<Vehicle> vehicles = vehicleRepository.findAllByQueryAndType(query.toLowerCase(), type.toLowerCase());
        return vehicles.stream()
                .filter(vehicle -> vehicle.isAvailable(dateFrom, dateTo))
                .toList();
    }

    public void validateAndSaveNewCar(NewCarDto car, MultipartFile file) throws FileUploadException {
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        String imgUrl = fileUploadService.uploadFile(file);
        CarModel model = car.getCarModel().getId() == null
                ? carModelService.save(car.getCarModel())
                : carModelService.getCarModelById(car.getCarModel().getId());

        switch (car.getCarType()) {
            case "sport" -> {
                SportCar sport = car.getSportCar();
                sport.setModel(model);
                sport.setImageUrl(imgUrl);
                sport.setDescription(car.getDescription());
                violations.addAll(validator.validate(car.getSportCar()));
                if(!violations.isEmpty()) throw new ConstraintViolationException(violations);
                vehicleRepository.save(sport);
            }
            case "city" -> {
                CityCar city = car.getCityCar();
                city.setModel(model);
                city.setImageUrl(imgUrl);
                city.setDescription(car.getDescription());
                violations.addAll(validator.validate(car.getCityCar()));
                if(!violations.isEmpty()) throw new ConstraintViolationException(violations);
                vehicleRepository.save(city);
            }
        }
    }
}
