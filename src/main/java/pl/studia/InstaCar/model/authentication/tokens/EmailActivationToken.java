package pl.studia.InstaCar.model.authentication.tokens;

import jakarta.persistence.*;

@Entity
@Table(name = "email_tokens")
public class EmailActivationToken extends BaseToken {
}
