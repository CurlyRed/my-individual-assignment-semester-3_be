package Marketplace.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidCredentialsException extends ResponseStatusException {
    public InvalidCredentialsException(String errorCause) {
        super(HttpStatus.BAD_REQUEST, errorCause);
    }
}
