package de.aittr.car_rent.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
                example = "2025-03-28T11:46")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @NotNull
        @FutureOrPresent
        LocalDateTime rentalStartDate,

        @Schema(
                description = "Booking rental end day and time",
                example = "2025-03-29T10:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @NotNull
        @FutureOrPresent
        LocalDateTime rentalEndDate,

        @Schema(description = "Customer info DTO")
        @NotNull
        CustomerResponseDto customerDto,

        @Schema(description = "Car info DTO")
        @NotNull
        CarResponseDto carDto,

        @Schema(description = "Car", example = "AVAILABLE")
        CarStatus carStatus,

        @Schema(description = "Booking status", example = "ACTIVE")
        BookingStatus bookingStatus,

        @Schema(
                description = "Booking creating day and time",
                example = "2025-03-25T11:46")
        LocalDateTime createBookingDate,

        @Schema(
                description = "Booking updating day and time",
                example = "2025-03-25T11:46"
        )
        LocalDateTime updateBookingDate,

        @Schema(
                description = "Booking total price",
                example = "1250.00"
        )
        BigDecimal totalPrice
) {}
    

   