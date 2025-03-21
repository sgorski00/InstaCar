package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.Car;
import pl.studia.InstaCar.repository.CarRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void saveCar(Car car) {
        car.setModel("AUDI");
        carRepository.save(car);
    }


    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public void saveAllCars(List<Car> cars) {
        carRepository.saveAll(cars);
    }

    public long count() {
        return carRepository.count();
    }

    public Car getCarById(long id) {
        return carRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Car is not found.")
        );
    }
}
