package de.aittr.car_rent.domain.dto;

import de.aittr.car_rent.domain.entity.CarFuelType;
import de.aittr.car_rent.domain.entity.CarStatus;
import de.aittr.car_rent.domain.entity.CarTransmissionType;
import de.aittr.car_rent.domain.entity.CarType;
import de.aittr.car_rent.validation.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Car entity with id, brand, model, year, type, fuelType, transmissionType, status, dayRentalPrice and carImage")
public record CarResponseDto(

        @Schema(
                description = "Car unique identifier",
                example = "123",
                accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Car brand", example = "VW")
        @Pattern(
                regexp = "[A-Za-z ]{1,}",
                message = "Car brand should be at list two characters length and start with capital letter")
        String brand,

        @Schema(description = "Car model", example = "Golf")
        @Pattern(
                regexp = "([A-Z][a-zA-Z0-9]{2,})(\\s+([A-Z][a-zA-Z0-9]{2,}))*",
                message = "Product title should be at least three characters long and start with capital letter")
        String model,

        @Schema(description = "Car build year", example = "2025")
        int year,

        @Schema(
                description = "Сar body type",
                example = "SEDAN")
        @ValidEnum(enumClass = CarType.class, message = "Invalid car type")
        String type,

        @Schema(
                description = "Car fuel type",
                example = "PETROL")
        @ValidEnum(enumClass = CarFuelType.class, message = "Invalid car fuel type")
        String fuelType,

        @Schema(
                description = "Car transmission type",
                example = "AUTOMATIC")
        @ValidEnum(enumClass = CarTransmissionType.class, message = "Invalid car transmission type")
        String transmissionType,

        @Schema(
                description = "Car state (used / deleted)",
                example = "true")
        boolean isActive,

        @Schema(
                description = "Car status",
                example = "RENTED")
        @ValidEnum(enumClass = CarStatus.class, message = "Invalid car status")
        String carStatus,

        @Schema(description = "Car day rental price", example = "150.00")
        @Positive(message = "Car rental price must be positive (more than zero)")
        BigDecimal dayRentalPrice,

        @Schema(
                description = "Car image",
                example = "https://shop-bucket.fra1.digitaloceanspaces.com/coconut-caf872c7-2ebd-4ec0-bd28-ff198091392c.png")
        String carImage) {

}
