package pl.studia.InstaCar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimpleRouter {

    @GetMapping("/about")
    public String showAbout(){
        return "about";
    }

    @GetMapping("/services")
    public String showServices(){
        return "services";
    }

    @GetMapping("/error")
    public String showError() {
        return "error";
    }

    @GetMapping("/")
    public String showHome() {
        return "index";
    }
}
