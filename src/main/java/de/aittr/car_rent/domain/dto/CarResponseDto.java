package de.aittr.car_rent.domain.dto;

import de.aittr.car_rent.domain.entity.CarFuelType;
import de.aittr.car_rent.domain.entity.CarTransmissionType;
import de.aittr.car_rent.domain.entity.CarType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Car entity with brand and model")
public record CarResponseDto(

        @Schema(
                description = "Car unique identifier",
                example = "123",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Long id,

        @Schema(description = "Car brand", example = "VW")
        String brand,

        @Schema(description = "Car model", example = "Golf")
        String model,

        @Schema(description = "Car build year", example = "2025")
        int year,

        @Schema(
                description = "Сar body type",
                example = "SEDAN"
              )
        CarType type,

        @Schema(
                description = "Car fuel type",
                example = "PETROL"
                )
        CarFuelType fuelType,

        @Schema(
                description = "Car transmission type",
                example = "AUTOMATIC")
        CarTransmissionType transmissionType,

        @Schema(
                description = "Car status",
                example = "RENTED")
        String carStatus,

        @Schema(description = "Car day rental price", example = "150.00")
        BigDecimal dayRentalPrice,

        @Schema(description = "Car image", example = "https://shop-bucket.fra1.digitaloceanspaces.com/coconut-caf872c7-2ebd-4ec0-bd28-ff198091392c.png")
        String carImage) {

}
