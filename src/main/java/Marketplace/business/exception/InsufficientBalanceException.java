package Marketplace.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InsufficientBalanceException extends ResponseStatusException {
    public InsufficientBalanceException(String errorCause) {
        super(HttpStatus.BAD_REQUEST, errorCause);
    }
}
