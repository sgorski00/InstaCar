package pl.studia.InstaCar.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailParseException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import pl.studia.InstaCar.config.exceptions.*;
import pl.studia.InstaCar.model.authentication.tokens.PasswordResetToken;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Log4j2
@ControllerAdvice
public class ExceptionController {

    private final MessageSource messageSource;

    @Autowired
    public ExceptionController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String userNotAllowedExceptionHandler(
            UsernameNotFoundException ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(StatusChangeException.class)
    public String statusChangeExceptionHandler(
            StatusChangeException ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/admin/rents";
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

    @ExceptionHandler(MailParseException.class)
    public String mailParseExceptionHandler(
            MailParseException ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/contact";
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(
            Exception ex,
            RedirectAttributes redirectAttributes
    ) {
        List<String> messages = new ArrayList<>();
        String errHeader = messageSource.getMessage("error.header", null, LocaleContextHolder.getLocale());
        messages.add(errHeader);
        log.error("An error occurred: {}", ex.getMessage());

        String message;
        if(ex.getClass().equals(NoResourceFoundException.class)){
            message = messageSource.getMessage("error.page.not.found", null, LocaleContextHolder.getLocale());
            messages.add(message);
            redirectAttributes.addFlashAttribute("code", 404);
        }

        if(ex.getClass().equals(NoSuchElementException.class)){
            message = messageSource.getMessage("error.element.not.found", null, LocaleContextHolder.getLocale());
            messages.add(message);
            redirectAttributes.addFlashAttribute("code", 500);
        }

        if(ex.getClass().equals(IllegalArgumentException.class)){
            message = messageSource.getMessage("error.illegal.argument", null, LocaleContextHolder.getLocale());
            messages.add(message);
            redirectAttributes.addFlashAttribute("code", 500);
        }

        if(ex.getClass().equals(AuthorizationDeniedException.class)) {
            message = messageSource.getMessage("error.authorization.denied", null, LocaleContextHolder.getLocale());
            messages.add(message);
            redirectAttributes.addFlashAttribute("code", 403);
        }

        if(ex.getClass().equals(NullPointerException.class)) {
            message = messageSource.getMessage("error.null.pointer", null, LocaleContextHolder.getLocale());
            messages.add(message);
            redirectAttributes.addFlashAttribute("code", 500);
        }

        ex.printStackTrace();
        redirectAttributes.addFlashAttribute("messages", messages);
        return "redirect:/error";
    }
}
