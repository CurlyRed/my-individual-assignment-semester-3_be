package Marketplace.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidRequestException extends ResponseStatusException {
    public InvalidRequestException(String errorCause) {
        super(HttpStatus.BAD_REQUEST, errorCause);
    }
}
