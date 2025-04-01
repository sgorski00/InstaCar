package pl.studia.InstaCar.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/error")
public class ErrorPageController {

    @GetMapping
    public String show() {
        return "error";
    }
}
