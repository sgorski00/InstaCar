package pl.studia.InstaCar.model.authentication.tokens;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "email_tokens")
public class EmailActivationToken extends BaseToken {
}
