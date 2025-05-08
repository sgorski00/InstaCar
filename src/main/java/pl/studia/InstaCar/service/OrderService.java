package pl.studia.InstaCar.service;

import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.InstaCar.model.Rent;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.dto.OrderDto;
import pl.studia.InstaCar.model.enums.RentStatus;
import pl.studia.InstaCar.repository.RentRepository;

import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final RentRepository rentRepository;
    private final UserDetailsService userDetailsService;
    private final CityService cityService;

    @Autowired
    public OrderService(RentRepository rentRepository, UserDetailsService userDetailsService, CityService cityService) {
        this.rentRepository = rentRepository;
        this.userDetailsService = userDetailsService;
        this.cityService = cityService;
    }

    @Transactional
    public Rent createOrder(OrderDto order, ApplicationUser user) {
        order.getUserDetails().setUser(user);
        userDetailsService.save(order.getUserDetails());
        Rent rent = Rent.builder()
                .vehicle(order.getCar())
                .totalCost(order.getTotalPrice())
                .rentDate(order.getPickUpDate())
                .returnDate(order.getReturnDate())
                .pickUpCity(cityService.getCityById(order.getPickUpCityId()))
                .returnCity(cityService.getCityById(order.getReturnCityId()))
                .user(user)
                .additionalInfo(order.getAdditionalInfo())
                .rentStatus(RentStatus.PENDING)
                .build();
        order.getCar().rent(rent);
        return rentRepository.save(rent);
    }

    public Rent getOrderById(Long orderId) {
        return rentRepository.findById(orderId).orElseThrow(
                () -> new NoSuchElementException("Nie odnaleziono wypożyczenia o id: " + orderId + ".")
        );
    }

    public void cancelOrderById(Long orderId) {
        Rent rent = getOrderById(orderId);
        rent.setRentStatus(RentStatus.CANCELLED);
        rentRepository.save(rent);
    }

    public void acceptOrderById(Long orderId) {
        Rent rent = getOrderById(orderId);
        rent.setRentStatus(RentStatus.ACTIVE);
        rentRepository.save(rent);
    }
}
