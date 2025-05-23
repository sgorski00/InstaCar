package pl.studia.InstaCar.controller.admin_panel;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
import pl.studia.InstaCar.service.VehicleService;
import pl.studia.InstaCar.utils.ListPaginator;
import pl.studia.InstaCar.utils.PaginationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/admin/cars")
public class AdminCarController {

    private final VehicleService vehicleService;
    private final CarModelService carModelService;
    private final ListPaginator<Vehicle> listPaginator;
    private final MessageSource messageSource;

    @Value("${default.pagination.pages.size}")
    private int visiblePages;

    @Autowired
    public AdminCarController(VehicleService vehicleService, CarModelService carModelService, ListPaginator<Vehicle> listPaginator, @Qualifier("messageSource") MessageSource messageSource) {
        this.vehicleService = vehicleService;
        this.carModelService = carModelService;
        this.listPaginator = listPaginator;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String showCars(
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "search", required = false) String query,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        List<Vehicle> cars;
        if(query != null && !query.trim().isBlank()) {
            cars = vehicleService.getAllCarsByQuery(query);
        } else {
            cars = vehicleService.getAllCars();
        }

        List<Vehicle> carsPaginatedList = listPaginator.getPaginatedList(cars,page, size);
        Page<Vehicle> carsPage = new PageImpl<>(carsPaginatedList, pageRequest, cars.size());
        int totalPages = carsPage.getTotalPages();
        int[] pageNumbers = PaginationUtils.getPageNumbers(page, visiblePages, totalPages);

        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("cars" , carsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("search", query);
        return "admin_panel/cars";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        List<CarModel> allModels = carModelService.getAllCarModels();
        Map<Long, String> modelMap = new HashMap<>();
        allModels.forEach(m -> modelMap.put(m.getId(), m.toString()));

        if(!model.containsAttribute("carDto")) model.addAttribute("carDto", new NewCarDto());
        model.addAttribute("models", modelMap);
        model.addAttribute("carTypes", CarType.values());
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("fuels", FuelType.values());
        return "admin_panel/add_car";
    }

    @PostMapping("/add")
    public String addCar(
            @Valid @ModelAttribute("carDto") NewCarDto car,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            redirectAttributes.addFlashAttribute("carDto", car);
            return "redirect:/admin/cars/add";
        }

        try {
            vehicleService.validateAndSaveNewCar(car, file);
        } catch (ConstraintViolationException e) {
            String errorMsg = e.getConstraintViolations().iterator().next().getMessage();
            redirectAttributes.addFlashAttribute("error", errorMsg);
            redirectAttributes.addFlashAttribute("carDto", car);
            return "redirect:/admin/cars/add";
        } catch (IllegalArgumentException | FileUploadException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("carDto", car);
            return "redirect:/admin/cars/add";
        }

        String message = messageSource.getMessage("attr.car.saved", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("info", message);
        return "redirect:/admin/cars";
    }


    @PostMapping("/delete/{id}")
    public String deleteCar(
            @PathVariable(value = "id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        vehicleService.deleteById(id);
        String message = messageSource.getMessage("attr.car.deleted", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("info", message);
        return "redirect:/admin/cars";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model
    ) {
        EditCarDto carDto = new EditCarDto(vehicleService.getCarById(id));
        log.info("edit carDto GET method: {}", carDto);
        if(!model.containsAttribute("car")) model.addAttribute("car", carDto);
        model.addAttribute("carTypes", CarType.values());
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("fuels", FuelType.values());
        return "admin_panel/edit_car";
    }

    @PostMapping("/edit")
    public String editCar(
            @Valid @ModelAttribute("car") EditCarDto carDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        log.debug("edit carDto POST method: {}", carDto);
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            redirectAttributes.addFlashAttribute("car", carDto);
            return "redirect:/admin/cars/edit/" + carDto.getId();
        }
        switch(carDto.getCarType().toUpperCase()) {
            case "SPORT" -> {
                SportCar car = (SportCar) vehicleService.getCarById(carDto.getId());
                vehicleService.save(carDto.mapToCar(car));
            }
            case "CITY" -> {
                CityCar car = (CityCar) vehicleService.getCarById(carDto.getId());
                vehicleService.save(carDto.mapToCar(car));
            }
            default -> {
                redirectAttributes.addFlashAttribute("car", carDto);
                String message=messageSource.getMessage("error.vehicle.type.not.supported", null, LocaleContextHolder.getLocale());
                redirectAttributes.addFlashAttribute("error", message);
                return "redirect:/admin/cars/edit/" + carDto.getId();
            }
        }
        String message = messageSource.getMessage("attr.car.saved", null, LocaleContextHolder.getLocale());
        redirectAttributes.addAttribute("info", message);
        return "redirect:/admin/cars";
    }
}
