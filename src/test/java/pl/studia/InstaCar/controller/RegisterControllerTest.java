package pl.studia.InstaCar.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.dto.UserRegistrationDto;
import pl.studia.InstaCar.service.tokens.EmailTokenService;
import pl.studia.InstaCar.service.UserRegistrationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    private UserRegistrationService userRegistrationService;

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
                .andExpect(flash().attributeExists("info"))
                .andExpect(redirectedUrl("/login"));

        verify(userRegistrationService, times(1)).registerUser(user);
    }

    @Test
    void shouldNotRegisterUserWhenNotPassedInPost() throws Exception {
        mvc.perform(post("/register"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("error"))
                .andExpect(redirectedUrl("/register"));

        verify(userRegistrationService, times(0)).registerUser(any(UserRegistrationDto.class));
    }

    @Test
    void shouldActivateAccount() throws Exception {
        mvc.perform(get("/activate")
                        .param("token", "correct_token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("info"))
                .andExpect(flash().attributeCount(1));
    }
}
