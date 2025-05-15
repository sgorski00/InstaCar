package pl.studia.InstaCar.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.model.UserDetails;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.service.OrderService;
import pl.studia.InstaCar.service.UserDetailsService;
import pl.studia.InstaCar.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final OrderService orderService;

    @Autowired
    public UserController(UserService userService, UserDetailsService userDetailsService, OrderService orderService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.orderService = orderService;
    }

    @GetMapping
    public String showUserDetails(
            Model model,
            Principal principal
    ) {
        ApplicationUser user = userService.findUserByUsername(principal.getName());
        UserDetails userDetails = user.getUserDetails();
        model.addAttribute("user", user);
        if(model.getAttribute("user_details") == null) model.addAttribute("user_details",
                userDetails != null ? userDetails : new UserDetails());
        model.addAttribute("rents", orderService.getLastOrdersForUser(user, 15));
        return "user";
    }

    @PostMapping("/user_details")
    public String addUserDetails(
            @ModelAttribute("user_details") @Valid UserDetails userDetails,
            BindingResult bindingResult,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        ApplicationUser user = userService.findUserByUsername(principal.getName());
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            redirectAttributes.addFlashAttribute("user_details", userDetails);
            return "redirect:/user";
        }

        userDetails.setUser(user);
        userDetailsService.save(userDetails);
        redirectAttributes.addFlashAttribute("info", "Dane kontaktowe zapisane pomyślnie");
        return "redirect:/user";
    }
}
