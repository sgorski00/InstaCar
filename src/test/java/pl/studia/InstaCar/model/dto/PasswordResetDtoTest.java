package pl.studia.InstaCar.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordResetDtoTest {

    @Test
    void shouldReturnTrueIfPasswordsAreEqual() {
        PasswordResetDto dto = new PasswordResetDto("token");
        dto.setPassword("newPassword");
        dto.setConfirmPassword("newPassword");

        boolean result = dto.arePasswordsEqual();

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfPasswordsAreNotEqual() {
        PasswordResetDto dto = new PasswordResetDto("token");
        dto.setPassword("newPassword");
        dto.setConfirmPassword("wrongPassword");

        boolean result = dto.arePasswordsEqual();

        assertFalse(result);
    }
}
