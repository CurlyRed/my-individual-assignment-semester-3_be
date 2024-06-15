package Marketplace.business.validators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    private final PasswordValidator passwordValidator = new PasswordValidator();

    @Test
    void testIsValid_withValidPassword_shouldReturnTrue() {
        // Given
        String password = "Valid1@Password";

        // When
        boolean result = passwordValidator.isValid(password);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsValid_withShortPassword_shouldReturnFalse() {
        // Given
        String password = "Shor1@";

        // When
        boolean result = passwordValidator.isValid(password);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValid_withPasswordWithoutUpperCase_shouldReturnFalse() {
        // Given
        String password = "valid1@password";

        // When
        boolean result = passwordValidator.isValid(password);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValid_withPasswordWithoutDigit_shouldReturnFalse() {
        // Given
        String password = "Valid@Password";

        // When
        boolean result = passwordValidator.isValid(password);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValid_withPasswordWithoutSpecialCharacter_shouldReturnFalse() {
        // Given
        String password = "Valid1Password";

        // When
        boolean result = passwordValidator.isValid(password);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValid_withNullPassword_shouldReturnFalse() {
        // Given
        String password = null;

        // When
        boolean result = passwordValidator.isValid(password);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValid_withEmptyPassword_shouldReturnFalse() {
        // Given
        String password = "";

        // When
        boolean result = passwordValidator.isValid(password);

        // Then
        assertFalse(result);
    }
}
