package pl.studia.InstaCar.model.authentication;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.studia.InstaCar.model.authentication.tokens.EmailActivationToken;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Log4j2
@Entity
@Data
@Table(name = "app_users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationUser implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    @Size(min = 5, max = 50, message = "Nazwa użytkownika musi mieć od 5 do 50 znaków")
    @Column(unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String username;

    @Email(message = "Adres email jest nieprawidłowy")
    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<EmailActivationToken> emailTokens;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider = AuthProvider.LOCAL;

    @Column(unique = true)
    private String providerId;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private pl.studia.InstaCar.model.UserDetails userDetails;

    @Column(nullable = false)
    private Timestamp createdAt;

    private Timestamp updatedAt;

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
    public void prePersist() {
        this.createdAt = Timestamp.from(Instant.now());
        encryptPassword();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
        encryptPassword();
    }

    public void encryptPassword() {
        if (password != null && !password.startsWith("$2a$")) {
            this.password = passwordEncoder.encode(password);
        }
    }

    @Override
    public boolean isEnabled() {
        return !this.provider.equals(AuthProvider.LOCAL) || this.emailTokens.stream()
                .anyMatch(EmailActivationToken::getIsUsed);
    }

    @Override
    public String toString() {
        return this.userDetails == null || this.userDetails.getFirstName() == null || this.userDetails.getLastName() == null
                ? this.username
                : this.userDetails.toString();

    }
}
