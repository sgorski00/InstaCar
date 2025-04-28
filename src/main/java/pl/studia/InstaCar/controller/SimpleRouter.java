package pl.studia.InstaCar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimpleRouter {

    @GetMapping("/about")
    public String showAbout(){
        return "about";
    }

    //TODO(send mails in standalone contact controller)
    @GetMapping("/contact")
    public String showContact(){
        return "contact";
    }

    @GetMapping("/services")
    public String showServices(){
        return "services";
    }
}
