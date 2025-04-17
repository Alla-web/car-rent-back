package de.aittr.car_rent.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "Booking entity with rentalStartDate, rentalEndDate and carId")
public record BookingRequestDto(

        @NotNull(message = "Rental start date cannot be null.")
        @FutureOrPresent(message = "Rental start date must be today or in the future.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @Schema(description = "Booking rental start day", example = "2025-04-09T00:00")
        LocalDateTime rentalStartDate,

        @NotNull(message = "Rental end date cannot be null.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @Schema(description = "Booking rental end day", example = "2025-04-20T10:00")
        LocalDateTime rentalEndDate,

        @NotNull(message = "Car ID cannot be null.")
        @Schema(description = "Car unique identifier", example = "3")
        Long carId
) {}
