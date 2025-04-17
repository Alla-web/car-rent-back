package de.aittr.car_rent.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class BookingNotFoundException extends RestApiException {

    public BookingNotFoundException(Long bookingId) {
        super("Booking wight id " + bookingId + " not found", HttpStatus.NOT_FOUND);
    }
}