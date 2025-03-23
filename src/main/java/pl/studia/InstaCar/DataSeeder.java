package pl.studia.InstaCar;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.studia.InstaCar.model.Vehicle;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.AuthProvider;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.service.VehicleService;
import pl.studia.InstaCar.service.RoleService;
import pl.studia.InstaCar.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final VehicleService vehicleService;
    private final Faker faker;

    @Autowired
    public DataSeeder(UserService userService, RoleService roleService, VehicleService vehicleService) {
        this.userService = userService;
        this.roleService = roleService;
        this.vehicleService = vehicleService;
        this.faker = new Faker();
    }

    @Override
    public void run(String... args) throws Exception {
        if(userService.count() < 3) {
            generateFakeUsers(100);
        }
//        if(vehicleService.count() < 3) {
//            generateFakeCars(100);
//        }
    }

//    private void generateFakeCars(int numOfCars) {
//        List<Vehicle> cars = new ArrayList<>();
//        for(int i = 0; i < numOfCars; i++) {
//            Vehicle car = new Vehicle();
//            car.setModel(faker.beer().name());
//            cars.add(car);
//        }
//        vehicleService.saveAllCars(cars);
//    }

    private void generateFakeUsers(int quantity) {
        List<ApplicationUser> users = new ArrayList<>();
        Role userRole = roleService.findByName("user");
        for(int i = 0; i<quantity; i++) {
            ApplicationUser user = new ApplicationUser();
            user.setRole(userRole);
            user.setEmail(faker.internet().emailAddress());
            user.setUsername(faker.name().username());
            user.setPassword(faker.internet().password());
            user.setProvider(AuthProvider.LOCAL);
            users.add(user);
        }
        userService.saveAll(users);
    }
}
