package Marketplace.business.validators;

import org.springframework.stereotype.Component;

@Component
public class PhoneNumberValidator {
    public boolean isValid(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        boolean isValid = phoneNumber.matches("\\d{10}");
        return isValid;
    }
}
