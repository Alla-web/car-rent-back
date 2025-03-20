package de.aittr.car_rent.exception_handling;

import de.aittr.car_rent.exception_handling.exceptions.CarNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionsHandler {

    public ResponseEntity<Response> handleException(CarNotFoundException e) {
        Response response = new Response();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


}
