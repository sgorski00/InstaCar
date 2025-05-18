package pl.studia.InstaCar.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private final MessageSource messageSource;

    @Getter
    @Value("${contact.email}")
    private String contactEmail;

    @Autowired
    public ContactController(EmailService emailService, @Qualifier("messageSource") MessageSource messageSource) {
        this.emailService = emailService;
        this.messageSource = messageSource;
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
            @Valid @ModelAttribute EmailDto email,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        email.setEmailTo(contactEmail);
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/contact";
        }
        emailService.sendEmail(email);
        String message = messageSource.getMessage("attr.message.sent", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("info", message);
        return "redirect:/contact";
    }
}
