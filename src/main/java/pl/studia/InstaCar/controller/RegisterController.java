package pl.studia.InstaCar.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.service.UserService;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/register")
@Log4j2
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegisterForm(
            Model model
    ) {
        model.addAttribute("user", new ApplicationUser());
        return "register";
    }

    @PostMapping
    public String registerUser(
            @ModelAttribute("user") ApplicationUser user,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.info("Failed to create new user: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("error", "Proszę wprowadzić poprawne dane.");
            return "redirect:/register";
        }

        try {
            userService.save(user);
            redirectAttributes.addFlashAttribute("info", "Proszę potwierdzić swój adres email poprzez link weryfikacyjny wysłany na adres: " + user.getEmail());
        }  catch (Exception e) {
            log.error(e.getCause());
            redirectAttributes.addFlashAttribute("error", "Coś poszło nie tak!");
            return "redirect:/register";
        }
        return "redirect:/login";
    }
}
