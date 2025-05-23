package pl.studia.InstaCar.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.studia.InstaCar.config.exceptions.ApiResponseException;
import pl.studia.InstaCar.model.Vehicle;
import pl.studia.InstaCar.model.dto.CarUpdateDto;
import pl.studia.InstaCar.service.VehicleService;
import pl.studia.InstaCar.utils.ListPaginator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/cars")
public class CarApiController {

    private final VehicleService vehicleService;
    private final MessageSource messageSource;

    @Autowired
    public CarApiController(VehicleService vehicleService, @Qualifier("messageSource") MessageSource messageSource) {
        this.vehicleService = vehicleService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> showCars(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "100") Integer size
    ) {
        List<Vehicle> cars = vehicleService.getAllCars();
        ListPaginator<Vehicle> listPaginator = new ListPaginator<>();
        List<Vehicle> carsPage = listPaginator.getPaginatedList(cars, page, size);
        return ResponseEntity.status(200).body(carsPage);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> showCar(
            @PathVariable(value = "id") Long id
    ) {
        try {
            Vehicle car = vehicleService.getCarById(id);
            return ResponseEntity.status(200).body(car);
        } catch (NoSuchElementException e) {
            throw new ApiResponseException(e.getMessage(), 404);
        }
    }

    @PostMapping
    public ResponseEntity<?> addCar(
            @RequestBody Vehicle vehicle
    ) {
        Vehicle savedVehicle = vehicleService.save(vehicle);
        Map<String, Object> response = new HashMap<>();
        String message = messageSource.getMessage("attr.car.saved", null, LocaleContextHolder.getLocale());
        response.put("message", message);
        response.put("id", savedVehicle.getId());
        return ResponseEntity.status(201)
                .body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> editCar(
            @PathVariable("id") Long id,
            @RequestBody CarUpdateDto carDto
    ) {
        Vehicle exisitngVehicle = vehicleService.getCarById(id);
        carDto.updateCar(exisitngVehicle);
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
