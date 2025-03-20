package de.aittr.car_rent.exception_handling.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RestApiException extends RuntimeException {


    private HttpStatus status;

    public RestApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public RestApiException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
