package de.aittr.car_rent.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingDto(
        Long id,

        @NotNull
        @FutureOrPresent(message = "Rental start date must be today or in the future.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime rentalStartDate,

        @NotNull
        @Future(message = "Rental end date must be after the start date.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime rentalEndDate,

        @NotNull(message = "Customer ID is required.")
        Long customerId,

        @NotNull(message = "Car ID is required.")
        Long carId,

        boolean booked,

        LocalDateTime createBookingDate,

        LocalDateTime updateBookingDate,


        BigDecimal totalPrice
) {

}
    

   