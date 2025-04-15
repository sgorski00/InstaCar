package pl.studia.InstaCar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.studia.InstaCar.service.WebClientService;

import java.util.concurrent.ExecutionException;

@Controller
public class HomeController {

    private final WebClientService webClientService;

    @Autowired
    public HomeController(WebClientService webClientService) {
        this.webClientService = webClientService;
    }

    @GetMapping("/")
    public String showHome(Model model)
    {
        try {
            model.addAttribute("rates", webClientService.getRates("EUR", "USD", "GBP").get());
        } catch (Exception e) {
            model.addAttribute("error", "Nie udało się pobrać walut");
        }
        return "index";
    }

}
