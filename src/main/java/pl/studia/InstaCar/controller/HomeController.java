package pl.studia.InstaCar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.model.Car;
import pl.studia.InstaCar.service.CarService;

@Controller
public class HomeController {

    private final CarService carService;

    @Autowired
    public HomeController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/")
    public String showHome(
            Model model
    ) {
        Car car = new Car(1L, "BMW");
        model.addAttribute("car", car);
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
        carService.saveCar(new Car());
        redirectAttributes.addFlashAttribute("info", "Pojazd został zapisany");
        return "redirect:/";
    }

}
