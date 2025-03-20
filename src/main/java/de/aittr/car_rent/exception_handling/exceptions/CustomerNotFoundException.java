package de.aittr.car_rent.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends RestApiException {
    public CustomerNotFoundException(Long id) {
        super(String.format("Customer with id %d not found", id), HttpStatus.NOT_FOUND);
    }
}
