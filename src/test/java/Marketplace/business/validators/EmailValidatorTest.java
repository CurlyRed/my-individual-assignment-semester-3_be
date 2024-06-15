package Marketplace.business.validators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmailValidatorTest {
    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void testValidEmail() {
        assertTrue(emailValidator.isValid("test@example.com"));
        assertTrue(emailValidator.isValid("user.name+tag+sorting@example.com"));
        assertTrue(emailValidator.isValid("user.name@example.co.uk"));
        assertTrue(emailValidator.isValid("user_name@example.com"));
        assertTrue(emailValidator.isValid("username@example.co.in"));
    }

    @Test
    void testInvalidEmail() {
        assertFalse(emailValidator.isValid("plainaddress"));
        assertFalse(emailValidator.isValid("@missingusername.com"));
        assertFalse(emailValidator.isValid("username123@gmail.a"));
    }
}

