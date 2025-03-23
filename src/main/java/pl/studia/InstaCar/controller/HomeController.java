package pl.studia.InstaCar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.service.VehicleService;

@Controller
public class HomeController {

    private final VehicleService vehicleService;

    @Autowired
    public HomeController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/")
    public String showHome(
            Model model
    ) {
        return "index";
    }

    @GetMapping("/car")
    public String showCar(){
        return "car";
    }

    @GetMapping("/save")
    public String save(
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("info", "Pojazd został zapisany");
        return "redirect:/";
    }

}
