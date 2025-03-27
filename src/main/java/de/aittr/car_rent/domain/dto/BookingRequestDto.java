package de.aittr.car_rent.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Booking entity with rentalStartDate, rentalEndDate and carId")
public record BookingRequestDto(

        @Schema(description = "Booking rental start day", example = "2025-03-28T11:46:01.774")
        LocalDateTime rentalStartDate,

        @Schema(description = "Booking rental end day", example = "2025-03-29T10:00:00.000")
        LocalDateTime rentalEndDate,

        @Schema(description = "Car unique identifier", example = "3")
        Long carId
) {}
