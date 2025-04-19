package pl.studia.InstaCar.controller.admin_panel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
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
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        return "admin_panel/users";
    }
}
