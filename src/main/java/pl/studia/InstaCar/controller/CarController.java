package pl.studia.InstaCar.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.studia.InstaCar.model.CityCar;
import pl.studia.InstaCar.model.SportCar;
import pl.studia.InstaCar.model.Vehicle;
import pl.studia.InstaCar.service.VehicleService;
import pl.studia.InstaCar.utils.ListPaginator;
import pl.studia.InstaCar.utils.PaginationUtils;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@Controller
@RequestMapping("/cars")
public class CarController {

    private final VehicleService vehicleService;
    private final ListPaginator<Vehicle> listPaginator;

    @Value("${default.pagination.pages.size}")
    private int visiblePages;

    @Autowired
    public CarController(VehicleService vehicleService, ListPaginator<Vehicle> listPaginator) {
        this.vehicleService = vehicleService;
        this.listPaginator = listPaginator;
    }

    @GetMapping
    public String showCars(
            @RequestParam(value = "size", defaultValue = "15") Integer size,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", defaultValue = "all") String type,
            @RequestParam(value = "pickDate", required = false) LocalDate pickDate,
            @RequestParam(value = "returnDate", required = false) LocalDate returnDate,
            Model model
    ) {
        if(pickDate == null) pickDate = LocalDate.now();
        if(returnDate == null) returnDate = LocalDate.now().plusDays(7);
        List<Vehicle> allCars = vehicleService.getAllCarsByQueryAndTypeAvailableInDate(keyword, type, pickDate, returnDate);
        List<Vehicle> carsPaginatedList = listPaginator.getPaginatedList(allCars,page, size);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Vehicle> carsPage = new PageImpl<>(carsPaginatedList, pageRequest, allCars.size());
        int totalPages = carsPage.getTotalPages();
        int[] pageNumbers = PaginationUtils.getPageNumbers(page, visiblePages, totalPages);

        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pickDate", pickDate);
        model.addAttribute("returnDate", returnDate);
        model.addAttribute("cars" , carsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageNumbers", pageNumbers);
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
        return "car-single";
    }
}
