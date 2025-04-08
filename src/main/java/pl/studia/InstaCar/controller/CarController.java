package pl.studia.InstaCar.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.config.exceptions.EntityValidationException;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.model.CityCar;
import pl.studia.InstaCar.model.SportCar;
import pl.studia.InstaCar.model.Vehicle;
import pl.studia.InstaCar.model.dto.NewCarDto;
import pl.studia.InstaCar.model.enums.CarType;
import pl.studia.InstaCar.model.enums.FuelType;
import pl.studia.InstaCar.model.enums.Transmission;
import pl.studia.InstaCar.service.CarModelService;
import pl.studia.InstaCar.service.FileUploadService;
import pl.studia.InstaCar.service.VehicleService;
import pl.studia.InstaCar.utils.ListPaginator;

import java.io.IOException;
import java.util.List;

@Log4j2
@Controller
@RequestMapping("/cars")
public class CarController {

    private final VehicleService vehicleService;
    private final CarModelService carModelService;
    private final FileUploadService fileUploadService;
    private final ListPaginator<Vehicle> listPaginator;

    @Autowired
    public CarController(VehicleService vehicleService, CarModelService carModelService, FileUploadService fileUploadService, ListPaginator<Vehicle> listPaginator) {
        this.vehicleService = vehicleService;
        this.carModelService = carModelService;
        this.fileUploadService = fileUploadService;
        this.listPaginator = listPaginator;
    }

    @GetMapping
    public String showCars(
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            Model model
    ) {
        List<Vehicle> allCars = vehicleService.getAllCars();
        List<Vehicle> carsPaginatedList = listPaginator.getPaginatedList(allCars,page, size);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Vehicle> carsPage = new PageImpl<>(carsPaginatedList, pageRequest, allCars.size());

        model.addAttribute("cars" , carsPage);
        return "cars";
    }

    @GetMapping("{id}")
    public String showCar(
            @PathVariable(value = "id") Long id,
            Model model
    ) {
        Vehicle car = vehicleService.getCarById(id);
        if(car instanceof SportCar sportCar) {
            model.addAttribute("car", sportCar);
        } else if (car instanceof CityCar cityCar){
            model.addAttribute("car", cityCar);
        }else {
            model.addAttribute("car", car);
        }
        return "car";
    }

    @GetMapping("/add")
    public String showAddForm(
            Model model
    ) {
        model.addAttribute("carDto", new NewCarDto());
        model.addAttribute("carModels", carModelService.getAllCarModels());
        model.addAttribute("carTypes", CarType.values());
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("fuels", FuelType.values());
        return "add_car";
    }

    @PostMapping("/add")
    public String addCar(
            @ModelAttribute("carDto") NewCarDto car,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) throws FileUploadException {
        String imgUrl = fileUploadService.uploadFile(file);
        CarModel model = carModelService.save(car.getCarModel());
        switch (car.getType()) {
            case "sport" -> {
                car.getSportCar().setModel(model);
                car.getSportCar().setImageUrl(imgUrl);
                vehicleService.save(car.getSportCar());
            }
            case "city" -> {
                car.getCityCar().setModel(model);
                car.getCityCar().setImageUrl(imgUrl);
                vehicleService.save(car.getCityCar());
            }
            default -> throw new EntityValidationException("Zły typ pojazdu", "/cars/add");
        }
        redirectAttributes.addFlashAttribute("info", "Pojazd został zapisany");
        return "redirect:/cars";
    }

    @PostMapping("/delete/{id}")
    public String deleteCar(
            @PathVariable(value = "id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        vehicleService.deleteById(id);
        redirectAttributes.addFlashAttribute("info", "Pojazd został usunięty");
        return "redirect:/cars";
    }

    @PostMapping("/edit/{id}")
    public String editCar(
            @ModelAttribute("carDto") NewCarDto car,
            @PathVariable(value = "id") Long id,
            Model model
    ) {
        switch(car.getType()) {
            case "sport" -> {
                car.getSportCar().setId(id);
                vehicleService.save(car.getSportCar());
            }
            case "city" -> {
                car.getCityCar().setId(id);
                vehicleService.save(car.getCityCar());
            }
            default -> throw new EntityValidationException("Zły typ pojazdu", "/cars/add");
        }
        model.addAttribute("info", "Pojazd został zapisany");
        return "redirect:/cars";
    }
}
