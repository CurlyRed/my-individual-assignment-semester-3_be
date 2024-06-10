package Marketplace.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyExistsException extends ResponseStatusException {
    public EmailAlreadyExistsException(String errorCause) {
        super(HttpStatus.BAD_REQUEST, errorCause);
    }
}
