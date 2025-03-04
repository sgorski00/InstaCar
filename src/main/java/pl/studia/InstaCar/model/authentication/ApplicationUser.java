package pl.studia.InstaCar.model.authentication;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "app_users")
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, max = 50)
    @Column(unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String username;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<EmailToken> emailTokens;

    @Transient
    private static PasswordEncoder passwordEncoder;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public static void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        ApplicationUser.passwordEncoder = passwordEncoder;
    }

    @PrePersist
    @PreUpdate
    public void encryptPassword() {
        if(password != null && !password.startsWith("2a$")) {
            this.password = passwordEncoder.encode(password);
        }
    }

    @Override
    public boolean isEnabled(){
        return this.emailTokens.stream()
                .anyMatch(EmailToken::getIsVerified);
    }
}
