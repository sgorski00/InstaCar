package pl.studia.InstaCar.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.AuthProvider;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.repository.UserRepository;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@Log4j2
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public OAuth2UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");

        ApplicationUser user = saveAndGetUser(email, provider, providerId);
        return new DefaultOAuth2User(
                user.getAuthorities(),
                oAuth2User.getAttributes(),
                "email"
        );
    }

    private ApplicationUser saveAndGetUser(String email, String provider, String providerId) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseGet(() -> {
                    ApplicationUser newUser = new ApplicationUser();
                    newUser.setUsername(email);
                    newUser.setEmail(email);
                    newUser.setProvider(AuthProvider.valueOf(provider.toUpperCase()));
                    newUser.setProviderId(providerId);
                    Role userRole = roleService.findByName("user");
                    newUser.setRole(userRole);
                    log.info("New OAuth2 user registered: {}", newUser);
                    return userRepository.save(newUser);
                });
    }

}
