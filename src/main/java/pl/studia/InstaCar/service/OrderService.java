package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.InstaCar.model.Rent;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.dto.OrderDto;
import pl.studia.InstaCar.model.enums.RentStatus;
import pl.studia.InstaCar.repository.RentRepository;
import pl.studia.InstaCar.service.event.OrderCreateEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final ApplicationEventPublisher eventPublisher;
    private final RentRepository rentRepository;
    private final UserDetailsService userDetailsService;
    private final CityService cityService;
    private final MessageSource messageSource;

    @Autowired
    public OrderService(ApplicationEventPublisher eventPublisher, RentRepository rentRepository, UserDetailsService userDetailsService, CityService cityService, @Qualifier("messageSource") MessageSource messageSource) {
        this.eventPublisher = eventPublisher;
        this.rentRepository = rentRepository;
        this.userDetailsService = userDetailsService;
        this.cityService = cityService;
        this.messageSource = messageSource;
    }

    @Transactional
    public Rent createOrder(OrderDto order, ApplicationUser user) {
        order.getUserDetails().setUser(user);
        userDetailsService.save(order.getUserDetails());
        Rent rent = Rent.builder()
                .vehicle(order.getCar())
                .rentDate(order.getPickUpDate())
                .returnDate(order.getReturnDate())
                .pickUpCity(cityService.getCityById(order.getPickUpCityId()))
                .returnCity(cityService.getCityById(order.getReturnCityId()))
                .user(user)
                .additionalInfo(order.getAdditionalInfo())
                .rentStatus(RentStatus.PENDING)
                .build();

        order.getCar().rent(rent, messageSource);
        Rent result = rentRepository.save(rent);
        eventPublisher.publishEvent(new OrderCreateEvent(this, rent));
        return result;
    }

    public Rent getOrderById(Long orderId) {
        return rentRepository.findById(orderId).orElseThrow(
                () -> {
                    String message = messageSource.getMessage("error.rent.not.found", null, LocaleContextHolder.getLocale());
                    return new NoSuchElementException(message + ": " + orderId);
                }
        );
    }

    public void cancelOrderById(Long orderId) {
        Rent rent = getOrderById(orderId);
        rent.setRentStatus(RentStatus.CANCELLED, messageSource);
        rentRepository.save(rent);
    }

    public void acceptOrderById(Long orderId) {
        Rent rent = getOrderById(orderId);
        rent.setRentStatus(RentStatus.ACTIVE, messageSource);
        rentRepository.save(rent);
    }

    @PreAuthorize("@orderSecurity.isOwner(#orderId, authentication.name)")
    public Rent getOrderIfOwner(Long orderId) {
        return getOrderById(orderId);
    }

    @PreAuthorize("@orderSecurity.isOwner(#orderId, authentication.name)")
    public void cancelOrderByIdIfOwner(Long orderId) {
        cancelOrderById(orderId);
    }

    @PreAuthorize("@orderSecurity.isOwner(#orderId, authentication.name)")
    public void acceptOrderByIdIfOwner(Long orderId) {
        acceptOrderById(orderId);
    }

    public List<Rent> getLastOrdersForUser(ApplicationUser user, int numOfRents) {
        List<Rent> rents = rentRepository.findAllByUserOrderByRentDateDesc(user);
        return rents.size() < numOfRents ? rents : rents
                .subList(0, numOfRents);
    }

    public Page<Rent> getAllRentsBetween(LocalDate dateFrom, LocalDate dateTo, Pageable pageable) {
        dateFrom = dateFrom == null ? LocalDate.now().minusMonths(12) : dateFrom;
        dateTo = dateTo == null ? LocalDate.now().plusDays(1) : dateTo;
        return rentRepository.findAllByRentDateBetweenOrderByRentDateDesc(dateFrom, dateTo, pageable);
    }

    public List<Rent> findAllRents() {
        return rentRepository.findAll();
    }

    public void saveAll(List<Rent> rents) {
        rentRepository.saveAll(rents);
    }

    public void cancelExpiredOrdersOlderThan(LocalDate date) {
        rentRepository.findAllByCreatedAtBefore(date).forEach(rent -> {
            if(rent.getRentStatus().equals(RentStatus.PENDING)) {
                rent.setRentStatus(RentStatus.CANCELLED, messageSource);
                rentRepository.save(rent);
            }
        });
    }
}
