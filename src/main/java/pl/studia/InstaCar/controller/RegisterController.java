package pl.studia.InstaCar.controller;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.model.dto.UserRegistrationDto;
import pl.studia.InstaCar.service.tokens.EmailTokenService;
import pl.studia.InstaCar.service.UserRegistrationService;

@Controller
@Log4j2
public class RegisterController {

    private final UserRegistrationService userRegistrationService;
    private final EmailTokenService emailTokenService;

    @Autowired
    public RegisterController(UserRegistrationService userRegistrationService, EmailTokenService emailTokenService) {
        this.userRegistrationService = userRegistrationService;
        this.emailTokenService = emailTokenService;
    }

    @GetMapping("/register")
    public String showRegisterForm(
            Model model
    ) {
        if(!model.containsAttribute("user")) model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") UserRegistrationDto user,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.info("Failed to create new user: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/register";
        }
        try {
            userRegistrationService.registerUser(user);
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/register";
        }
        redirectAttributes.addFlashAttribute("info", "Proszę potwierdzić swój adres email poprzez link weryfikacyjny wysłany na adres: " + user.getEmail());
        return "redirect:/login";
    }

    @GetMapping("/activate")
    public String activate(
            @RequestParam(value = "token") String token,
            RedirectAttributes redirectAttributes
    ) {
        emailTokenService.setTokenActivated(token);
        redirectAttributes.addFlashAttribute("info", "Aktywacja zakończona pomyślnie. Możesz się zalogować!");
        return "redirect:/login";
    }
}
