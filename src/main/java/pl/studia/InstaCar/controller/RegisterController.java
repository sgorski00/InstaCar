package pl.studia.InstaCar.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.service.tokens.EmailTokenService;
import pl.studia.InstaCar.service.UserRegistrationService;

import java.util.NoSuchElementException;

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
        model.addAttribute("user", new ApplicationUser());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") ApplicationUser user,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.info("Failed to create new user: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("error", "Proszę wprowadzić poprawne dane.");
            return "redirect:/register";
        }

        try {
            userRegistrationService.registerUser(user);
            redirectAttributes.addFlashAttribute("info", "Proszę potwierdzić swój adres email poprzez link weryfikacyjny wysłany na adres: " + user.getEmail());
        } catch (ConstraintViolationException e) {
            log.error(e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Nazwa użytkownika lub email są już w użyciu.");
            return "redirect:/register";
        } catch (IllegalArgumentException | NoSuchElementException e) {
            log.error(e.getCause());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        } catch (Exception e) {
            log.error(e.getCause());
            redirectAttributes.addFlashAttribute("error", "Coś poszło nie tak!");
            return "redirect:/register";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate")
    public String activate(
            @RequestParam(value = "token") String token,
            RedirectAttributes redirectAttributes
    ) {
        try {
            emailTokenService.setTokenActivated(token);
            redirectAttributes.addFlashAttribute("info", "Aktywacja zakończona pomyślnie. Możesz się zalogować!");
        } catch (IllegalArgumentException | NoSuchElementException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        } catch (Exception e) {
            log.error(e.getCause());
            redirectAttributes.addFlashAttribute("error", "Aktywacja nie powiodła się.");
            return "redirect:/register";
        }
        return "redirect:/login";
    }
}
