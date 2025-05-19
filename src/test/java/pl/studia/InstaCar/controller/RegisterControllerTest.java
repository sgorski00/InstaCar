package pl.studia.InstaCar.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.studia.InstaCar.model.authentication.tokens.EmailActivationToken;
import pl.studia.InstaCar.model.dto.UserRegistrationDto;
import pl.studia.InstaCar.repository.EmailTokenRepository;
import pl.studia.InstaCar.service.tokens.EmailTokenService;
import pl.studia.InstaCar.service.UserRegistrationService;

import java.util.Locale;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegisterController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RegisterControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private EmailTokenService emailTokenService;

    @MockitoBean
    private EmailTokenRepository emailTokenRepository;

    @MockitoBean
    private UserRegistrationService userRegistrationService;

    @MockitoBean
    private MessageSource messageSource;

    @Test
    void shouldShowRegistrationForm() throws Exception {
        mvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void shouldRegisterUser() throws Exception {
        UserRegistrationDto user = new UserRegistrationDto();
        user.setUsername("username");
        user.setPassword("password");
        user.setConfirmPassword("password");
        user.setEmail("email@email.com");

        mvc.perform(post("/register")
                        .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(messageSource, times(1)).getMessage(eq("attr.register.success"), nullable(Object[].class), any(Locale.class));
        verify(userRegistrationService, times(1)).registerUser(user);
    }

    @Test
    void shouldNotRegisterUserWhenNotPassedInPost() throws Exception {
        mvc.perform(post("/register"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("error", "user"))
                .andExpect(redirectedUrl("/register"));

        verify(userRegistrationService, times(0)).registerUser(any(UserRegistrationDto.class));
    }

    @Test
    void shouldNotRegisterUserWhenIncompatibleData() throws Exception {
        UserRegistrationDto user = new UserRegistrationDto();
        user.setUsername("username");
        user.setPassword(" ");
        user.setConfirmPassword(" ");
        user.setEmail("email@email.com");
        mvc.perform(post("/register")
                        .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("error", "user"))
                .andExpect(redirectedUrl("/register"));

        verify(userRegistrationService, times(0)).registerUser(any(UserRegistrationDto.class));
    }

    @Test
    void shouldActivateAccount() throws Exception {
        mvc.perform(get("/activate")
                        .param("token", "correct_token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeCount(1));

        verify(messageSource, times(1)).getMessage(eq("attr.activate.success"), nullable(Object[].class), any(Locale.class));
        verify(emailTokenService, times(1)).setTokenActivated(anyString());
    }

    @Test
    void shouldNotActivateAccount() throws Exception {
        when(emailTokenRepository.findFirstByTokenOrderByIdDesc(anyString())).thenReturn(Optional.empty());
        mvc.perform(get("/activate")
                        .param("token", "wrong_token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(emailTokenRepository, times(0)).save(any(EmailActivationToken.class));
    }
}
