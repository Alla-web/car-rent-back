package de.aittr.car_rent.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class CarNotFoundException extends RestApiException {

    public CarNotFoundException(Long id) {
        super(String.format("Car with id %d not found", id), HttpStatus.NOT_FOUND);
    }

    public CarNotFoundException(
            String brand,
            String model,
            String type,
            String fuelType,
            String transmissionType,
            String carStatus) {
        super(String.format("Car with %s not found",
                brand != null ? "brand " + brand :
                        model != null ? "model " + model :
                                type != null ? "type " + type :
                                        fuelType != null ? "fuel type " + fuelType :
                                                transmissionType != null ? "transmission type " + transmissionType :
                                                        carStatus != null ? "status " + carStatus :
                                                                "unknown property"
        ), HttpStatus.NOT_FOUND);
    }

    public CarNotFoundException(int year) {
        super(String.format("Car with year %d not found", year), HttpStatus.NOT_FOUND);
    }

    public CarNotFoundException(BigDecimal dayRentalPrice) {
        super(String.format("Car with dayRentalPrice %.2f not found", dayRentalPrice), HttpStatus.NOT_FOUND);
    }

}
