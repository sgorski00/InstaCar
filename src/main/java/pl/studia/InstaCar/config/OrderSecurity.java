package pl.studia.InstaCar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.studia.InstaCar.service.OrderService;

@Component("orderSecurity")
public class OrderSecurity {

    private final OrderService orderService;

    @Autowired
    public OrderSecurity(OrderService orderService) {
        this.orderService = orderService;
    }

    public boolean isOwner(Long orderId, String username) {
        return orderService.getOrderById(orderId)
                .getUser()
                .getUsername()
                .equals(username);
    }
}
