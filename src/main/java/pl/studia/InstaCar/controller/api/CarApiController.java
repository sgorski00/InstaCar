package pl.studia.InstaCar.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.model.Vehicle;
import pl.studia.InstaCar.model.dto.CarUpdateDto;
import pl.studia.InstaCar.service.VehicleService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/cars")
public class CarApiController {

    private final VehicleService vehicleService;

    @Autowired
    public CarApiController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> showCars() {
        List<Vehicle> cars = vehicleService.getAllCars();
        return ResponseEntity.status(200).body(cars);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> showCar(
            @PathVariable(value = "id") Long id
    ) {
        try {
            Vehicle car = vehicleService.getCarById(id);
            return ResponseEntity.status(200).body(car);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(302)
                    .header("Location", "/login")
                    .build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addCar(
            @ModelAttribute("model") CarModel model,
            @ModelAttribute("vehicle") Vehicle vehicle
    ) {
        vehicle.setModel(model);
        vehicleService.save(vehicle);
        return ResponseEntity.status(201)
                .body("Pojazd został dodany");
    }

    @PutMapping("{id}")
    public ResponseEntity<?> editCar(
            @PathVariable("id") Long id,
            @ModelAttribute("vehicle") CarUpdateDto carDto
    ) {
        Vehicle exisitngVehicle = vehicleService.getCarById(id);
        exisitngVehicle.updateCarDetails(carDto);
        vehicleService.save(exisitngVehicle);
        return ResponseEntity.status(201)
                .body("Pojazd został zapisany");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCar(@PathVariable("id") Long id){
        vehicleService.deleteById(id);
        return ResponseEntity.status(200).body("Pojazd został usunięty");
    }
}
