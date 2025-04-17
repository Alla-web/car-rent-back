package de.aittr.car_rent.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends RestApiException {
    public CustomerNotFoundException() {
        super("Customer is not found", HttpStatus.NOT_FOUND);
    }
}
