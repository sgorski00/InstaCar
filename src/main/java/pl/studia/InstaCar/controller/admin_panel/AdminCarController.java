package pl.studia.InstaCar.controller.admin_panel;

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
import pl.studia.InstaCar.model.dto.EditCarDto;
import pl.studia.InstaCar.model.dto.NewCarDto;
import pl.studia.InstaCar.model.enums.CarType;
import pl.studia.InstaCar.model.enums.FuelType;
import pl.studia.InstaCar.model.enums.Transmission;
import pl.studia.InstaCar.service.CarModelService;
import pl.studia.InstaCar.service.FileUploadService;
import pl.studia.InstaCar.service.VehicleService;
import pl.studia.InstaCar.utils.ListPaginator;
import pl.studia.InstaCar.utils.PaginationUtils;

import java.util.List;
@Log4j2
@Controller
@RequestMapping("/admin/cars")
public class AdminCarController {

    private final VehicleService vehicleService;
    private final CarModelService carModelService;
    private final FileUploadService fileUploadService;
    private final ListPaginator<Vehicle> listPaginator;

    @Autowired
    public AdminCarController(VehicleService vehicleService, CarModelService carModelService, FileUploadService fileUploadService, ListPaginator<Vehicle> listPaginator) {
        this.vehicleService = vehicleService;
        this.carModelService = carModelService;
        this.fileUploadService = fileUploadService;
        this.listPaginator = listPaginator;
    }

    @GetMapping
    public String showCars(
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "search", required = false) String query,
            Model model
    ) {
        int totalPages = 1;
        if(query != null && !query.trim().isBlank()) {
            List<Vehicle> queriedCars = vehicleService.getAllCarsByQuery(query);
            model.addAttribute("cars" , queriedCars);
        } else {
            PageRequest pageRequest = PageRequest.of(page - 1, size);
            List<Vehicle>allCars = vehicleService.getAllCars();
            List<Vehicle> carsPaginatedList = listPaginator.getPaginatedList(allCars,page, size);
            Page<Vehicle> carsPage = new PageImpl<>(carsPaginatedList, pageRequest, allCars.size());
            totalPages = carsPage.getTotalPages();
            int visiblePages = 5;
            int[] pageNumbers = PaginationUtils.getPageNumbers(page, visiblePages, totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("cars" , carsPage);
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        model.addAttribute("totalPages", totalPages);
        return "admin_panel/cars";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("carDto", new NewCarDto());
        model.addAttribute("carTypes", CarType.values());
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("fuels", FuelType.values());
        return "admin_panel/add_car";
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
            default -> throw new EntityValidationException("Zły typ pojazdu", "admin//cars/add");
        }
        redirectAttributes.addFlashAttribute("info", "Pojazd został zapisany");
        return "redirect:/admin/cars";
    }

    @PostMapping("/delete/{id}")
    public String deleteCar(
            @PathVariable(value = "id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        vehicleService.deleteById(id);
        redirectAttributes.addFlashAttribute("info", "Pojazd został usunięty");
        return "redirect:/admin/cars";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model
    ) {
        EditCarDto carDto = new EditCarDto(vehicleService.getCarById(id));
        log.debug("edit carDto GET method: {}", carDto);
        model.addAttribute("car", carDto);
        model.addAttribute("carTypes", CarType.values());
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("fuels", FuelType.values());
        return "admin_panel/edit_car";
    }

    @PostMapping("/edit")
    public String editCar(
            @ModelAttribute("car") EditCarDto carDto,
            Model model
    ) {
        log.debug("edit carDto POST method: {}", carDto);
        switch(carDto.getCarType().toUpperCase()) {
            case "SPORT" -> {
                SportCar car = (SportCar) vehicleService.getCarById(carDto.getId());
                vehicleService.save(carDto.mapToCar(car));
            }
            case "CITY" -> {
                CityCar car = (CityCar) vehicleService.getCarById(carDto.getId());
                vehicleService.save(carDto.mapToCar(car));
            }
            default -> throw new EntityValidationException("Zły typ pojazdu", "/admin/cars/edit/" + carDto.getId());
        }
        model.addAttribute("info", "Pojazd został zapisany");
        return "redirect:/admin/cars";
    }
}
