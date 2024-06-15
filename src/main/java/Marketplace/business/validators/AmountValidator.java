package Marketplace.business.validators;

import org.springframework.stereotype.Component;

@Component
public class AmountValidator {
    public boolean isValid(Double value) {
        boolean isValid = value > 0;
        return isValid;
    }
}
