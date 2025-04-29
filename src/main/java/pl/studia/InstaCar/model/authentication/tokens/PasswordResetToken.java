package pl.studia.InstaCar.model.authentication.tokens;

import jakarta.persistence.*;

@Entity
@Table(name = "password_tokens")
public class PasswordResetToken extends BaseToken {
}
