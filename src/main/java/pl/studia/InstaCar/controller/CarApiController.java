package pl.studia.InstaCar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.studia.InstaCar.model.Car;
import pl.studia.InstaCar.service.CarService;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

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

    @GetMapping("{id}")
    public ResponseEntity<?> showCar(
            @PathVariable(value = "id") Long id
    ) {
        try {
            Car car = carService.getCarById(id);
            return ResponseEntity.status(200).body(car);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(302)
                    .header("Location", "/login")
                    .build();
        }
    }

}
