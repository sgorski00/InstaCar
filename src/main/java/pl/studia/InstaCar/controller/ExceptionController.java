package pl.studia.InstaCar.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.config.exceptions.EntityValidationException;
import pl.studia.InstaCar.config.exceptions.TokenIllegalArgumentException;
import pl.studia.InstaCar.model.authentication.tokens.PasswordResetToken;

import java.util.NoSuchElementException;

@Log4j2
@ControllerAdvice
@RequestMapping("/error")
public class ExceptionController {

    @ExceptionHandler(UsernameNotFoundException.class)
    public String userNotAllowedExceptionHandler(
            UsernameNotFoundException ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(TokenIllegalArgumentException.class)
    public String tokenIllegalArgumentExceptionHandler(
            TokenIllegalArgumentException ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        if(ex.getTokenClass().equals(PasswordResetToken.class)) {
            return "redirect:/login/reset";
        }
        return "redirect:/login";
    }

    @ExceptionHandler(EntityValidationException.class)
    public String entityValidationExceptionHandler(
            EntityValidationException ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        if(ex.getRedirectUrl().startsWith("redirect")){
            return ex.getRedirectUrl();
        }
        return "redirect:" + ex.getRedirectUrl();
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(
            Exception ex,
            RedirectAttributes redirectAttributes
    ) {
        String message ="";
        log.error("An error occurred: {}", ex.getMessage());
        if(ex.getClass().equals(NoSuchElementException.class)){
            message = "Wybrany element nie istnieje.\n";
        }

        if(ex.getClass().equals(IllegalArgumentException.class)){
            message = "Nieprawidłowe dane.\n";
        }
        message += ex.getMessage();
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/error";
    }

    @GetMapping("/error")
    public String show() {
        return "error";
    }

}