package de.aittr.car_rent.exception_handling.exceptions;

public class CarNotAvailableException extends RestApiException {
    public CarNotAvailableException(String carIsAlreadyBooked) {
        super("Car is already booked");
    }
}
