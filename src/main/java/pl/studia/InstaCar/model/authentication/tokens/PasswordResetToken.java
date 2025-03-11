package pl.studia.InstaCar.model.authentication.tokens;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "password_tokens")
public class PasswordResetToken extends BaseToken {
}
