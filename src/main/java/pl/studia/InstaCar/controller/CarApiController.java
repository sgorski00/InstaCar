package pl.studia.InstaCar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.InstaCar.model.Car;
import pl.studia.InstaCar.service.CarService;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarApiController {

    private final CarService carService;

    @Autowired
    public CarApiController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<Car>> showCars() {
        List<Car> cars = carService.getAllCars();
        return ResponseEntity.status(200).body(cars);
    }
}
