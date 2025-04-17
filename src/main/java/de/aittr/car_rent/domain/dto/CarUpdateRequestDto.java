package de.aittr.car_rent.domain.dto;

import de.aittr.car_rent.domain.entity.CarStatus;
import de.aittr.car_rent.validation.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Car update DTO with id, car status, dayRentalPrice and carImage")

public record CarUpdateRequestDto(
        @Schema(
                description = "Car unique identifier",
                example = "123",
                accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(
                description = "Car status",
                example = "RENTED")
        @ValidEnum(enumClass = CarStatus.class, message = "Invalid car status")
        String carStatus,

        @Schema(description = "Car day rental price", example = "150.00")
        @Positive(message = "Car rental price must be positive (more than zero)")
        @DecimalMax(value = "5000.00", message = "Car rental price must not exceed 5000")
        BigDecimal dayRentalPrice) {}
