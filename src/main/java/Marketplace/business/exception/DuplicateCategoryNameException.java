package Marketplace.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicateCategoryNameException extends ResponseStatusException {
    public DuplicateCategoryNameException(String errorCause) {
        super(HttpStatus.BAD_REQUEST, errorCause);
    }
}
