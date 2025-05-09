package pl.studia.InstaCar.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.studia.InstaCar.model.Rent;
import pl.studia.InstaCar.model.Vehicle;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.dto.OrderDto;
import pl.studia.InstaCar.service.*;

import java.security.Principal;
import java.time.LocalDate;

@Log4j2
@Controller
@RequestMapping("/order")
public class OrderController {

    private final VehicleService vehicleService;
    private final UserService userService;
    private final OrderService orderService;
    private final CityService cityService;

    @Autowired
    public OrderController(VehicleService vehicleService, UserService userService, OrderService orderService, CityService cityService) {
        this.vehicleService = vehicleService;
        this.userService = userService;
        this.orderService = orderService;
        this.cityService = cityService;
    }

    @GetMapping("/create")
    public String showCreateForm(
            @RequestParam Long carId,
            @RequestParam LocalDate pickDate,
            @RequestParam LocalDate returnDate,
            Model model,
            Principal principal
    ) {
        ApplicationUser user = userService.findUserByUsername(principal.getName());
        Vehicle car = vehicleService.getCarById(carId);
        OrderDto order = new OrderDto(user.getUserDetails(), car, pickDate, returnDate);

        model.addAttribute("car", car);
        model.addAttribute("pickDate", pickDate);
        model.addAttribute("returnDate", returnDate);
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("order", order);
        return "order";
    }

    @PostMapping("/create")
    public String createOrder(
            @ModelAttribute("order") OrderDto order,
            @RequestParam Long carId,
            Principal principal
    ) {
        ApplicationUser user = userService.findUserByUsername(principal.getName());
        order.setCar(vehicleService.getCarByIdWithRents(carId));
        Rent rent = orderService.createOrder(order, user);
        return "redirect:/order/summary?order=" + rent.getId();
    }

    @GetMapping("/summary")
    public String showSummary(
            @RequestParam(name = "order") Long orderId,
            Model model
    ) {
        Rent rent = orderService.getOrderIfOwner(orderId);
        model.addAttribute("order", rent);
        return "order-summary";
    }

    @PostMapping("/decline")
    public String declineOrder(
            @RequestParam(name = "orderId") Long orderId
    ) {
        orderService.cancelOrderByIdIfOwner(orderId);
        return "redirect:/order/summary?order=" + orderId;
    }

    @PostMapping("/accept")
    public String acceptOrder(
            @RequestParam(name = "orderId") Long orderId
    ) {
        orderService.acceptOrderByIdIfOwner(orderId);
        return "redirect:/order/summary?order=" + orderId;
    }
}
