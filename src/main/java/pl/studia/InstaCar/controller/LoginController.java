package pl.studia.InstaCar.controller;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.dto.PasswordResetDto;
import pl.studia.InstaCar.service.PasswordResetService;
import pl.studia.InstaCar.service.tokens.PasswordTokenService;
import pl.studia.InstaCar.service.UserService;

import java.util.NoSuchElementException;

@Log4j2
@Controller
public class LoginController {

    private final PasswordResetService passwordResetService;
    private final UserService userService;
    private final PasswordTokenService passwordTokenService;

    @Autowired
    public LoginController(PasswordResetService passwordResetService, UserService userService, PasswordTokenService passwordTokenService) {
        this.passwordResetService = passwordResetService;
        this.userService = userService;
        this.passwordTokenService = passwordTokenService;
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
            passwordResetService.generateToken(user);
        }catch (NoSuchElementException e) {
            log.info("Password email reset link not send for identifier: {}", identifier);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login/reset";
        }
        redirectAttributes.addFlashAttribute("info", "Wysłano maila z instrukcją resetowania hasła");
        return "redirect:/login";
    }

    @GetMapping("/login/reset/form")
    public String showPasswordResetForm(
            @RequestParam(name = "token") String token,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        try {
            ApplicationUser user = passwordTokenService.getUserForToken(token);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("pwdResetDto", new PasswordResetDto());
            model.addAttribute("token", token);
            return "password_reset_form";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("info", e.getMessage());
            return "redirect:/login";
        }
    }

    @PostMapping("/login/reset/form")
    public String sendPasswordResetForm(
            @RequestParam(name = "token") String token,
            @Valid @ModelAttribute("pwdResetDto") PasswordResetDto passwordResetDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Podane hasło nie spełnia wymagań");
            return "redirect:/login/reset/form?token=" + token;
        }

        if(!passwordResetDto.arePasswordsEqual()){
            redirectAttributes.addFlashAttribute("error", "Hasła nie są takie same.");
            return "redirect:/login/reset/form?token=" + token;
        }

        ApplicationUser user = passwordTokenService.getUserForToken(token);
        passwordResetService.changePassword(user, passwordResetDto, token);
        redirectAttributes.addFlashAttribute("info", "Hasło zostało zmienione");
        return "redirect:/login";
    }
}
