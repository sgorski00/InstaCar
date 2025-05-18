package pl.studia.InstaCar.controller;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.dto.PasswordResetDto;
import pl.studia.InstaCar.service.PasswordResetService;
import pl.studia.InstaCar.service.tokens.PasswordTokenService;
import pl.studia.InstaCar.service.UserService;

@Log4j2
@Controller
@RequestMapping("/login")
public class LoginController {

    private final PasswordResetService passwordResetService;
    private final UserService userService;
    private final PasswordTokenService passwordTokenService;
    private final MessageSource messageSource;

    @Autowired
    public LoginController(PasswordResetService passwordResetService, UserService userService, PasswordTokenService passwordTokenService, MessageSource messageSource) {
        this.passwordResetService = passwordResetService;
        this.userService = userService;
        this.passwordTokenService = passwordTokenService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String showLoginPage(
            @RequestParam(required = false, name = "error") String error,
            Model model
    ) {
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "login";
    }

    @GetMapping("/reset")
    public String showPasswordResetPage(
            @RequestParam(required = false, name = "error") String error,
            Model model
    ) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "password_reset";
    }

    @PostMapping("/reset")
    public String sendResetPasswordMail(
            @RequestParam(name = "identify") String identifier,
            RedirectAttributes redirectAttributes
    ) {
        ApplicationUser user = userService.findByUsernameOrEmail(identifier);
        passwordResetService.generateToken(user);
        String message = messageSource.getMessage("attr.reset.message.sent", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("info", message);
        return "redirect:/login";
    }

    @GetMapping("/reset/form")
    public String showPasswordResetForm(
            @RequestParam(name = "token") String token,
            Model model
    ) {
        ApplicationUser user = passwordTokenService.getUserForToken(token);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("pwdResetDto", new PasswordResetDto());
        model.addAttribute("token", token);
        return "password_reset_form";
    }

    @PostMapping("/reset/form")
    public String sendPasswordResetForm(
            @RequestParam(name = "token") String token,
            @Valid @ModelAttribute("pwdResetDto") PasswordResetDto passwordResetDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "redirect:/login/reset/form?token=" + token;
        }

        if (!passwordResetDto.arePasswordsEqual()) {
            String message = messageSource.getMessage("validation.password.repeat.match", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("error", message);
            redirectAttributes.addFlashAttribute("pwdResetDto", passwordResetDto);
            return "redirect:/login/reset/form?token=" + token;
        }

        ApplicationUser user = passwordTokenService.getUserForToken(token);
        passwordResetService.changePassword(user, passwordResetDto, token);
        String message = messageSource.getMessage("attr.password.saved", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("info", message);
        return "redirect:/login";
    }
}
