package pl.studia.InstaCar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.service.RoleService;
import pl.studia.InstaCar.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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
}
