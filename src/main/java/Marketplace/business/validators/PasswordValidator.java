package Marketplace.business.validators;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordValidator {
    private final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$"
    );

    public boolean isValid(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        boolean isValid = PASSWORD_PATTERN.matcher(password).matches();
        return isValid;
    }
}
