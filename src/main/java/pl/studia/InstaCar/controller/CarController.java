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
import pl.studia.InstaCar.service.WebClientService;
import pl.studia.InstaCar.utils.ComparatorUtils;
import pl.studia.InstaCar.utils.ListPaginator;
import pl.studia.InstaCar.utils.PaginationUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/cars")
public class CarController {

    private final VehicleService vehicleService;
    private final ListPaginator<Vehicle> listPaginator;
    private final WebClientService webClientService;

    @Value("${default.pagination.pages.size}")
    private int visiblePages;

    @Autowired
    public CarController(VehicleService vehicleService, ListPaginator<Vehicle> listPaginator, WebClientService webClientService) {
        this.vehicleService = vehicleService;
        this.listPaginator = listPaginator;
        this.webClientService = webClientService;
    }

    @GetMapping
    public String showCars(
            @RequestParam(value = "size", defaultValue = "15") Integer size,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", defaultValue = "all") String type,
            @RequestParam(value = "pickDate", required = false) LocalDate pickDate,
            @RequestParam(value = "returnDate", required = false) LocalDate returnDate,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            Model model
    ) {
        if(pickDate == null) pickDate = LocalDate.now();
        if(returnDate == null) returnDate = LocalDate.now().plusDays(7);
        List<Vehicle> allCars = new ArrayList<>(vehicleService.getAllCarsByQueryAndTypeAvailableInDate(keyword, type, pickDate, returnDate));
        allCars.sort(ComparatorUtils.getVehicleComparator(sort));
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
        model.addAttribute("sort", sort);
        return "cars";
    }

    @GetMapping("{id}")
    public String showCar(
            @PathVariable(value = "id") Long id,
            Model model
    ) {
        Vehicle car = vehicleService.getCarById(id);
        List<String> currencies = List.of("EUR", "USD", "GBP");
        Map<String, String> prices = new HashMap<>();
        webClientService.getRates(currencies)
                .forEach(r -> prices.put(r.getCode(), r.getNewestPriceFormatted(car.getPrice())));
        if(car instanceof SportCar sportCar) {
            model.addAttribute("car", sportCar);
        } else if (car instanceof CityCar cityCar){
            model.addAttribute("car", cityCar);
        }else {
            model.addAttribute("car", car);
        }
        model.addAttribute("prices", prices);
        return "car-single";
    }
}
