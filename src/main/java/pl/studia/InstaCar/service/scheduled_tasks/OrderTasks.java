package pl.studia.InstaCar.service.scheduled_tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.studia.InstaCar.service.OrderService;

import java.time.LocalDate;

@Component
public class OrderTasks {

    private final OrderService orderService;

    @Autowired
    public OrderTasks(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cancelExpiredOrders() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        orderService.cancelExpiredOrdersOlderThan(yesterday);
    }
}
