package pl.studia.InstaCar.model.authentication.tokens;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import pl.studia.InstaCar.model.authentication.ApplicationUser;

import java.time.LocalDateTime;

@Log4j2
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String token;

    @Column(name ="is_used", nullable = false)
    private Boolean isUsed;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
