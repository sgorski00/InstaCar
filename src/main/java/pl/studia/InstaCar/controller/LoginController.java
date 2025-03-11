package pl.studia.InstaCar.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.service.PasswordTokenService;
import pl.studia.InstaCar.service.UserService;

import java.util.NoSuchElementException;

@Log4j2
@Controller
public class LoginController {

    private final PasswordTokenService passwordTokenService;
    private final UserService userService;

    @Autowired
    public LoginController(PasswordTokenService passwordTokenService, UserService userService) {
        this.passwordTokenService = passwordTokenService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(required = false, name = "error") String error,
            Model model
    ) {
        if(error != null) {
            model.addAttribute("error", true);
        }
        return "login";
    }

    @GetMapping("/login/reset")
    public String showPasswordResetPage(
            @RequestParam(required = false, name = "error") String error,
            Model model
    ) {
        if(error != null) {
            model.addAttribute("error", error);
        }
        return "password_reset";
    }

    @PostMapping("/login/reset")
    public String sendResetPasswordMail(
            @RequestParam(name = "identify") String identifier,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ApplicationUser user = userService.findByUsernameOrEmail(identifier);
            passwordTokenService.saveTokenForUser(user);
        }catch (NoSuchElementException e) {
            log.info("Password email reset link not send for identifier: {}", identifier);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login/reset";
        }
        redirectAttributes.addFlashAttribute("info", "Wysłano maila z instrukcją resetowania hasła");
        return "redirect:/login";
    }
}
