package pl.studia.InstaCar.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.InstaCar.config.exceptions.ApiResponseException;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.service.CarModelService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/models")
public class CarModelApiController {

    private final CarModelService carModelService;

    @Autowired
    public CarModelApiController(CarModelService carModelService) {
        this.carModelService = carModelService;
    }

    @GetMapping("{id}")
    public ResponseEntity<CarModel> showModels(
            @PathVariable(value = "id") Long id
    ){
        try {
            return ResponseEntity.ok(carModelService.getCarModelById(id));
        }catch (NoSuchElementException e){
            throw new ApiResponseException(e.getMessage(), 404);
        }
    }
}
