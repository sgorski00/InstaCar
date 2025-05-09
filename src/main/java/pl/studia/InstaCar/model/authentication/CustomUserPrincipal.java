package pl.studia.InstaCar.model.authentication;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomUserPrincipal implements UserDetails, OAuth2User {

    private final String username;
    private final String password;
    private final String displayName;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final boolean enabled;

    public CustomUserPrincipal(ApplicationUser user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getAuthorities();
        this.enabled = user.isEnabled();
        this.attributes = null;
        this.displayName = user.getUserDetails() == null ? username : user.getUserDetails().toString();
    }

    public CustomUserPrincipal(ApplicationUser user, Map<String, Object> attributes) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getAuthorities();
        this.enabled = user.isEnabled();
        this.attributes = attributes;
        this.displayName = user.getUserDetails() == null ? username : user.getUserDetails().toString();
    }

    @Override
    public String getName() {
        return this.displayName;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
