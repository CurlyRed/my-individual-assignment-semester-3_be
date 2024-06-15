package Marketplace.business.validators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberValidatorTest {

    private final PhoneNumberValidator phoneNumberValidator = new PhoneNumberValidator();

    @Test
    void testIsValid_withValidPhoneNumber_shouldReturnTrue() {
        // Given
        String phoneNumber = "1234567890";

        // When
        boolean result = phoneNumberValidator.isValid(phoneNumber);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsValid_withNullPhoneNumber_shouldReturnFalse() {
        // Given
        String phoneNumber = null;

        // When
        boolean result = phoneNumberValidator.isValid(phoneNumber);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValid_withEmptyPhoneNumber_shouldReturnFalse() {
        // Given
        String phoneNumber = "";

        // When
        boolean result = phoneNumberValidator.isValid(phoneNumber);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValid_withShortPhoneNumber_shouldReturnFalse() {
        // Given
        String phoneNumber = "123456789";

        // When
        boolean result = phoneNumberValidator.isValid(phoneNumber);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValid_withLongPhoneNumber_shouldReturnFalse() {
        // Given
        String phoneNumber = "12345678901";

        // When
        boolean result = phoneNumberValidator.isValid(phoneNumber);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValid_withPhoneNumberContainingLetters_shouldReturnFalse() {
        // Given
        String phoneNumber = "12345abcde";

        // When
        boolean result = phoneNumberValidator.isValid(phoneNumber);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValid_withPhoneNumberContainingSpecialCharacters_shouldReturnFalse() {
        // Given
        String phoneNumber = "12345@#$%^";

        // When
        boolean result = phoneNumberValidator.isValid(phoneNumber);

        // Then
        assertFalse(result);
    }
}
