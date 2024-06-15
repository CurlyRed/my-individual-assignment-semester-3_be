package Marketplace.business.validators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AmountValidatorTest {

    private final AmountValidator amountValidator = new AmountValidator();

    @Test
    void testIsValid_withPositiveValue_shouldReturnTrue() {
        // Given
        Double value = 10.0;

        // When
        boolean result = amountValidator.isValid(value);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsValid_withZeroValue_shouldReturnFalse() {
        // Given
        Double value = 0.0;

        // When
        boolean result = amountValidator.isValid(value);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValid_withNegativeValue_shouldReturnFalse() {
        // Given
        Double value = -10.0;

        // When
        boolean result = amountValidator.isValid(value);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValid_withNullValue_shouldThrowException() {
        // Given
        Double value = null;

        // When & Then
        assertThrows(NullPointerException.class, () -> amountValidator.isValid(value));
    }
}
