package pl.studia.InstaCar.model.authentication;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "email_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String token;

    @Column(name = "verified", nullable = false)
    private Boolean isVerified;
}
