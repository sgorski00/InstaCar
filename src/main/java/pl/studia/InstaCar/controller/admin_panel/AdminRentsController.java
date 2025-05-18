package pl.studia.InstaCar.controller.admin_panel;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.model.Rent;
import pl.studia.InstaCar.service.OrderService;
import pl.studia.InstaCar.utils.PaginationUtils;

import java.time.LocalDate;

@Log4j2
@Controller
@RequestMapping("/admin/rents")
public class AdminRentsController {

    private final MessageSource messageSource;
    @Value("${default.pagination.pages.size}")
    private int visiblePages;

    private final OrderService orderService;

    @Autowired
    public AdminRentsController(OrderService orderService, @Qualifier("messageSource") MessageSource messageSource) {
        this.orderService = orderService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String show(
            @RequestParam(value = "size", defaultValue = "30") Integer size,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) LocalDate dateTo,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<Rent> allRents = orderService.getAllRentsBetween(dateFrom, dateTo, pageRequest);
        int totalPages = allRents.getTotalPages();
        int[] pageNumbers = PaginationUtils.getPageNumbers(page, visiblePages, totalPages);

        model.addAttribute("rents", allRents);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);
        return "admin_panel/rents";
    }

    @PostMapping("/cancel/{id}")
    public String cancelRent(
            @PathVariable Long id,
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo,
            RedirectAttributes redirectAttributes
    ) {
        orderService.cancelOrderById(id);
        String message = messageSource.getMessage("attr.rent.canceled", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("info", message + ": " + id);
        return "redirect:/admin/rents?dateFrom=" + dateFrom + "&dateTo=" + dateTo;
    }

    @GetMapping("{id}")
    public String showOrder(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("rent", orderService.getOrderById(id));
        return "admin_panel/rent";
    }
}
