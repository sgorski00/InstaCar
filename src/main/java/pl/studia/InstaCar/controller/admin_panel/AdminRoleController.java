package pl.studia.InstaCar.controller.admin_panel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.studia.InstaCar.service.RoleService;

@Controller
@RequestMapping("/admin/roles")
public class AdminRoleController {

    private final RoleService roleService;

    @Autowired
    public AdminRoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public String show(
            Model model
    ) {
        model.addAttribute("roles", roleService.findAll());
        return "admin_panel/roles";
    }
}
