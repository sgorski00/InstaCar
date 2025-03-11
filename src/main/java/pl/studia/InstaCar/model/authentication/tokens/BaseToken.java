package pl.studia.InstaCar.model.authentication.tokens;

import jakarta.persistence.*;
import lombok.*;
import pl.studia.InstaCar.model.authentication.ApplicationUser;

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
}
