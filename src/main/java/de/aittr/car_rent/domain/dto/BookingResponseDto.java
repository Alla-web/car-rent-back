package de.aittr.car_rent.domain.dto;

import de.aittr.car_rent.domain.entity.BookingStatus;
import de.aittr.car_rent.domain.entity.CarStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Booking entity with id, rentalStartDate, rentalEndDate, customerId, carId, carStatus, createBookingDate, updateBookingDate and totalPrice")
public record BookingResponseDto(

        @Schema(
                description = "Booking unique identifier",
                example = "123",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Long id,

        @Schema(
                description = "Booking start day and time",
                example = "2025-03-28T11:46:01.774")
        @NotNull
        @FutureOrPresent
        LocalDateTime rentalStartDate,

        @Schema(
                description = "Booking rental end day and time",
                example = "2025-03-29T10:00:00.000")
        @NotNull
        @FutureOrPresent
        LocalDateTime rentalEndDate,

        @Schema(description = "Customer unique identifier", example = "5")
        @NotNull
        Long customerId,

        @Schema(description = "Car unique identifier", example = "3")
        @NotNull
        Long carId,

        @Schema(description = "Car", example = "AVAILABLE")
        CarStatus carStatus,

        @Schema(description = "Booking status", example = "ACTIVE")
        BookingStatus bookingStatus,

        @Schema(
                description = "Booking creating day and time",
                example = "2025-03-25T11:46:01.774")
        LocalDateTime createBookingDate,

        @Schema(
                description = "Booking updating day and time",
                example = "2025-03-25T11:46:01.774"
        )
        LocalDateTime updateBookingDate,

        @Schema(
                description = "Booking total price",
                example = "1250.00"
        )
        BigDecimal totalPrice
) {}
    

   