package pl.studia.InstaCar.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import pl.studia.InstaCar.config.exceptions.EntityValidationException;
import pl.studia.InstaCar.config.exceptions.TokenIllegalArgumentException;
import pl.studia.InstaCar.model.authentication.tokens.PasswordResetToken;
import pl.studia.InstaCar.service.FileUploadService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Log4j2
@ControllerAdvice
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

    @ExceptionHandler(FileUploadException.class)
    public String fileUploadExceptionHandler(
            FileUploadException ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/cars";
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(
            Exception ex,
            RedirectAttributes redirectAttributes
    ) {
        List<String> messages = new ArrayList<>();
        messages.add("Wystąpił nieoczekiwany błąd:");
        log.error("An error occurred: {}", ex.getMessage());

        if(ex.getClass().equals(NoResourceFoundException.class)){
            messages.add("Strona nie istnieje");
            redirectAttributes.addFlashAttribute("code", 404);
            return "redirect:/";
        }

        if(ex.getClass().equals(NoSuchElementException.class)){
            messages.add("Wybrany element nie istnieje");
        }

        if(ex.getClass().equals(IllegalArgumentException.class)){
            messages.add("Nieprawidłowe dane");
        }

        if(ex.getClass().equals(AuthorizationDeniedException.class)) {
            messages.add("Brak uprawnień do żądanych zasobów");
        }

        ex.printStackTrace();
        redirectAttributes.addFlashAttribute("messages", messages);
        return "redirect:/error";
    }
}
