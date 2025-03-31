package pl.studia.InstaCar.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

@Log4j2
@ControllerAdvice
@RequestMapping("/error")
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(
            Exception ex,
            Model model
    ) {
        log.error("An error occurred: {}", ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNoHandlerFoundException(NoHandlerFoundException ex, Model model) {
        log.error("No handler found: {}", ex.getMessage());
        model.addAttribute("class", ex.getClass().getName());
        model.addAttribute("message", ex.getMessage());
        return "error";
    }
}
