package pl.studia.InstaCar.controller.admin_panel;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.model.UserDetails;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.model.dto.EditUserDto;
import pl.studia.InstaCar.service.RoleService;
import pl.studia.InstaCar.service.UserService;
import pl.studia.InstaCar.utils.PaginationUtils;

@Log4j2
@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminUserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showUsers(
            @RequestParam(value = "size", defaultValue = "50") Integer size,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "search", required = false) String query,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<ApplicationUser> usersPage = userService.findAllPaged(query, pageRequest);

        int visiblePages = 5;
        int[] pageNumbers = PaginationUtils.getPageNumbers(page, visiblePages, usersPage.getTotalPages());

        model.addAttribute("users", usersPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("search", query);
        return "admin_panel/users";
    }

    @PostMapping("/delete")
    public String deleteUser(
            @RequestParam("id") Long userId,
            RedirectAttributes redirectAttributes
    ) {
        userService.deleteUserById(userId);
        redirectAttributes.addFlashAttribute("info", "Użytkownik został usunięty");
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable("id") Long userId,
            Model model
    ) {
        ApplicationUser user = userService.findUserById(userId);
        model.addAttribute("userNotEditable", user);
        model.addAttribute("user", new EditUserDto(user));
        model.addAttribute("roles", roleService.findAll());
        return "admin_panel/edit_user";
    }

    @PostMapping("/edit/{id}")
    public String editUser(
            @PathVariable("id") Long id,
            @ModelAttribute("user") EditUserDto userDto,
            RedirectAttributes redirectAttributes
    ) {
        ApplicationUser userToEdit = userService.findUserById(id);
        Role role = roleService.findById(userDto.getRoleId());
        userDto.updateUser(userToEdit, role);
        userService.save(userToEdit);
        redirectAttributes.addFlashAttribute("message", "Użytkownik edytowany pomyślnie.");
        return "redirect:/admin/users/edit/" + id;
    }
}
