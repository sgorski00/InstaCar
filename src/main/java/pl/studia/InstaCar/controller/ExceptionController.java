package pl.studia.InstaCar.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import pl.studia.InstaCar.config.exceptions.ApiResponseException;
import pl.studia.InstaCar.config.exceptions.EntityValidationException;
import pl.studia.InstaCar.config.exceptions.NotAvailableException;
import pl.studia.InstaCar.config.exceptions.TokenIllegalArgumentException;
import pl.studia.InstaCar.model.authentication.tokens.PasswordResetToken;

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

    @ExceptionHandler(NotAvailableException.class)
    public String notAvailableExceptionHandler(
            UsernameNotFoundException ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/cars";
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
        return "redirect:/admin/cars";
    }

    @ExceptionHandler(ApiResponseException.class)
    public ResponseEntity<ProblemDetail> apiResponseExceptionHandler(
            ApiResponseException ex
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(ex.getCode()),
                ex.getMessage()
        );
        return ResponseEntity.status(ex.getCode()).body(problemDetail);
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
        }

        if(ex.getClass().equals(NoSuchElementException.class)){
            messages.add("Wybrany element nie istnieje");
            redirectAttributes.addFlashAttribute("code", 500);
        }

        if(ex.getClass().equals(IllegalArgumentException.class)){
            messages.add("Nieprawidłowe dane");
            redirectAttributes.addFlashAttribute("code", 500);
        }

        if(ex.getClass().equals(AuthorizationDeniedException.class)) {
            messages.add("Brak uprawnień do żądanych zasobów");
            redirectAttributes.addFlashAttribute("code", 403);
        }

        if(ex.getClass().equals(NullPointerException.class)) {
            messages.add("Brak żądanych zasobów");
            redirectAttributes.addFlashAttribute("code", 500);
        }

        ex.printStackTrace();
        redirectAttributes.addFlashAttribute("messages", messages);
        return "redirect:/error";
    }
}
