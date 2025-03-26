package pl.studia.InstaCar.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordResetDtoTest {

    @Test
    void shouldReturnTrueIfPasswordsAreEqual() {
        PasswordResetDto dto = new PasswordResetDto();
        dto.setPassword("newPassword");
        dto.setRepeatPassword("newPassword");

        boolean result = dto.arePasswordsEqual();

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfPasswordsAreNotEqual() {
        PasswordResetDto dto = new PasswordResetDto();
        dto.setPassword("newPassword");
        dto.setRepeatPassword("wrongPassword");

        boolean result = dto.arePasswordsEqual();

        assertFalse(result);
    }
}
