package pl.studia.InstaCar;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.model.Vehicle;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.AuthProvider;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.model.enums.CarType;
import pl.studia.InstaCar.model.enums.FuelType;
import pl.studia.InstaCar.model.enums.Transmission;
import pl.studia.InstaCar.service.CarModelService;
import pl.studia.InstaCar.service.VehicleService;
import pl.studia.InstaCar.service.RoleService;
import pl.studia.InstaCar.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final CarModelService carModelService;
    private final Faker faker;
    private final Random random;

    @Autowired
    public DataSeeder(UserService userService, RoleService roleService, CarModelService carModelService) {
        this.userService = userService;
        this.roleService = roleService;
        this.carModelService = carModelService;
        this.faker = new Faker();
        this.random = new Random();
    }

    @Override
    public void run(String... args) throws Exception {
        generateFakeUsers(100);
        List<CarModel> models = generateCarModels(15);
    }

    private List<CarModel> generateCarModels(int quantity) {
        List<CarModel> models = new ArrayList<>();
        if(carModelService.count() < 3) {
            for (int i = 0; i < quantity; i++) {
                CarModel carModel = new CarModel();
                carModel.setModelName(faker.country().name());
                carModel.setCarType(CarType.values()[random.nextInt(CarType.values().length)]);
                carModel.setBrand(faker.company().name());
                carModel.setDoors(random.nextInt(2, 5));
                carModel.setSeats(random.nextInt(2, 9));
                carModel.setFuelType(FuelType.values()[random.nextInt(FuelType.values().length)]);
                carModel.setTransmission(Transmission.values()[random.nextInt(Transmission.values().length)]);

                models.add(carModel);
            }
            carModelService.saveAll(models);
        }
        return models;
    }

    private void generateFakeUsers(int quantity) {
        if(userService.count() < 2) {
            List<ApplicationUser> users = new ArrayList<>();
            Role userRole = roleService.findByName("user");
            for (int i = 0; i < quantity; i++) {
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
}
