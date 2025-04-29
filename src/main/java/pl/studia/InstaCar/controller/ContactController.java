package pl.studia.InstaCar.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.studia.InstaCar.model.dto.EmailDto;
import pl.studia.InstaCar.service.EmailService;

@Controller
@RequestMapping("/contact")
public class ContactController {

    private final EmailService emailService;

    @Getter
    @Value("${contact.email}")
    private String contactEmail;

    @Autowired
    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public String show(
            Model model
    ) {
        model.addAttribute("email", new EmailDto());
        return "contact";
    }

    @PostMapping
    public String sendEmail(
            @ModelAttribute EmailDto email,
            RedirectAttributes redirectAttributes
    ) {
        email.setEmailTo(contactEmail);
        emailService.sendEmail(email);
        redirectAttributes.addFlashAttribute("info", "Wiadomość wysłana pomyślnie");
        return "redirect:/contact";
    }
}
