package de.aittr.car_rent.exception_handling;

import de.aittr.car_rent.exception_handling.exceptions.RestApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Response> handleException(RestApiException ex) {
        Response response = new Response(ex.getMessage());
        return new ResponseEntity<>(response, ex.getStatus());
    }
}
