package pl.studia.InstaCar;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.studia.InstaCar.model.*;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.AuthProvider;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.model.enums.CarType;
import pl.studia.InstaCar.model.enums.FuelType;
import pl.studia.InstaCar.model.enums.RentalStatus;
import pl.studia.InstaCar.model.enums.Transmission;
import pl.studia.InstaCar.service.*;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final CarModelService carModelService;
    private final Faker faker;

    private static final String[] CAR_BRANDS = {
            "Toyota","BMW","Mercedes","Ford","Audi","Volkswagen","Honda","Hyundai"
    };

    private static final String[] CAR_MODELS = {
            "Mondeo","Corolla","Stilo","911","Passat","Ceed","Sandero","Ibiza","i30", "E47", "A4", "3008"
    };
    private final VehicleService vehicleService;
    private final CityService cityService;

    @Autowired
    public DataSeeder(UserService userService, RoleService roleService, CarModelService carModelService, VehicleService vehicleService, CityService cityService) {
        this.userService = userService;
        this.roleService = roleService;
        this.carModelService = carModelService;
        this.faker = new Faker();
        this.vehicleService = vehicleService;
        this.cityService = cityService;
    }

    @Override
    public void run(String... args) throws Exception {
        generateFakeUsers(100);
        List<CarModel> models = generateCarModels(15);
        List<Vehicle> vehicles = generateVehicle(20, 30, models);
        List<City> cities = generateCities(10);
    }

    private List<City> generateCities(int numOfCities) {
        List<City> cities = new ArrayList<>();
        if (cityService.count() < 3) {
            for (int i = 0; i < numOfCities; i++) {
                City city = new City();
                city.setName(faker.address().cityName());
                cities.add(city);
            }
            cityService.saveAll(cities);
        }
        return cities;
    }

    private List<Vehicle> generateVehicle(int numOfSportCars, int numOfCityCars, List<CarModel> models) {
        List<Vehicle> vehicles = new ArrayList<>();
        if(vehicleService.count() < 3) {
            for(int i = 0; i < numOfSportCars; i++) {
                SportCar sportCar = new SportCar();
                sportCar.setAcceleration(faker.number().randomDouble(2, 0, 8));
                sportCar.setHorsePower(faker.number().numberBetween(200, 800));
                sportCar.setTopSpeed(faker.number().numberBetween(190, 350));
                setVehicleGenericFields(sportCar, models);
                vehicles.add(sportCar);
                vehicleService.save(sportCar);
            }

            for(int i = 0; i < numOfCityCars; i++) {
                CityCar cityCar = new CityCar();
                cityCar.setTrunkCapacity(faker.number().numberBetween(150, 500));
                setVehicleGenericFields(cityCar, models);
                vehicles.add(cityCar);
                vehicleService.save(cityCar);
            }
        }
        return vehicles;
    }

    private void setVehicleGenericFields(Vehicle vehicle, List<CarModel> models) {
        vehicle.setColor(faker.color().name());
        vehicle.setEngine(faker.number().numberBetween(1800, 4700) + "cc");
        vehicle.setLicensePlate(faker.letterify("??? ?????").toUpperCase());
        vehicle.setModel(faker.options().option(models.toArray(new CarModel[0])));
        vehicle.setPrice(faker.number().numberBetween(1, 1000));
        vehicle.setProductionYear(faker.number().numberBetween(2016, 2025));
        vehicle.setStatus(faker.options().option(RentalStatus.values()));
        vehicle.setVin(faker.bothify("1HGCM82633A######"));
        vehicle.setImageUrl("/uploads/car-" + faker.number().numberBetween(1, 12) + ".jpg");
    }

    private List<CarModel> generateCarModels(int quantity) {
        List<CarModel> models = new ArrayList<>();
        if(carModelService.count() < 3) {
            for (int i = 0; i < quantity; i++) {
                CarModel carModel = new CarModel();
                carModel.setModelName(faker.options().option(CAR_MODELS));
                carModel.setCarType(faker.options().option(CarType.values()));
                carModel.setBrand(faker.options().option(CAR_BRANDS));
                carModel.setDoors(faker.number().numberBetween(2,6));
                carModel.setSeats(faker.number().numberBetween(2,9));
                carModel.setFuelType(faker.options().option(FuelType.values()));
                carModel.setTransmission(faker.options().option(Transmission.values()));

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
