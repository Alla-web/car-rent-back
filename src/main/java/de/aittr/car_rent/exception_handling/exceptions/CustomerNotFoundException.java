package de.aittr.car_rent.exception_handling.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super(String.format("Customer with id %d not found", id));
    }
}
