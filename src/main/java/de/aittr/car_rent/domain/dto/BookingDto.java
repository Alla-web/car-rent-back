package de.aittr.car_rent.domain.dto;

import de.aittr.car_rent.domain.entity.Car;
import de.aittr.car_rent.domain.entity.CarStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingDto(
        Long id,
        @NotNull
        @FutureOrPresent
        LocalDateTime rentalStartDate,
        @NotNull
        @FutureOrPresent
        LocalDateTime rentalEndDate,
        @NotNull
        Long customerId,
        @NotNull
        Long carId,
        CarStatus carStatus,
        LocalDateTime createBookingDate,
        LocalDateTime updateBookingDate,
        BigDecimal totalPrice
) {

}
    

   