package pl.studia.InstaCar.init;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.service.RoleService;
import pl.studia.InstaCar.service.UserService;

import java.util.NoSuchElementException;

@Component
@Log4j2
public class UsersInitializer implements ApplicationRunner {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersInitializer(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(ApplicationArguments args) {
        initializeRoles();
        initializeUsers();
    }

    private void initializeUsers() {
        if(userService.isEmpty()){
            Role adminRole = roleService.findByName("admin");
            Role userRole = roleService.findByName("user");

            addUser("admin", "admin", "admin@gmail.com", adminRole);
            addUser("jkowalski", "kowalski123", "kowalski@oulook.com", userRole);
        }
    }

    private void initializeRoles() {
        if(roleService.isEmpty()){
            Role adminRole = new Role();
            adminRole.setName("ADMIN");

            Role userRole = new Role();
            userRole.setName("USER");

            roleService.saveAll(adminRole, userRole);
        }
    }

    private void addUser(
            String username,
            String password,
            String email,
            Role role
    ) {
        try {
            ApplicationUser user = new ApplicationUser();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setRole(role);
            userService.save(user);
        } catch (NoSuchElementException e){
            log.error(e.getMessage());
        }
    }
}
