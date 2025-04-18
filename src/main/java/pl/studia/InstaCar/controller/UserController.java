package pl.studia.InstaCar.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.config.exceptions.EntityValidationException;
import pl.studia.InstaCar.model.UserDetails;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.service.RoleService;
import pl.studia.InstaCar.service.UserDetailsService;
import pl.studia.InstaCar.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public UserController(UserService userService, RoleService roleService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public String showUsers(
            @RequestParam(value = "size", defaultValue = "50") Integer size,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<ApplicationUser> usersPage = userService.findAllPaged(pageRequest);
        model.addAttribute("users", usersPage);
        return "users";
    }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @GetMapping("{id}")
    public String showUserDetails(
            @PathVariable("id") Long id,
            Model model
    ) {
        ApplicationUser user = userService.findUserById(id);
        UserDetails userDetails = user.getUserDetails();
        model.addAttribute("user", user);
        model.addAttribute("user_details",
                userDetails != null ? userDetails : new UserDetails());
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
            throw new EntityValidationException("Podane dane nie spełniają wymagań.", "/users/" + user.getId());
        }

        userDetails.setUser(user);
        userDetailsService.save(userDetails);
        redirectAttributes.addFlashAttribute("info", "Dane kontaktowe zapisane pomyślnie");
        return "redirect:/users/" + user.getId();
    }
}
