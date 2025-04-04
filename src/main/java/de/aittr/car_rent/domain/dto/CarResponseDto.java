package de.aittr.car_rent.domain.dto;

import de.aittr.car_rent.domain.entity.CarFuelType;
import de.aittr.car_rent.domain.entity.CarTransmissionType;
import de.aittr.car_rent.domain.entity.CarType;
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
                // 1-я буква должна быть заглавная, далее идут буквы маленькие и поле может
                // далее минимум 1 символ и может состоять из нескольких слов
                regexp = "[A-Za-z ]{1,}",
                message = "Car brand should be at list two characters length and start with capital letter")
        String brand,

        @Schema(description = "Car model", example = "Golf")
        @Pattern(
                // 1-я буква — любая латинская буква (заглавная или строчная)
                // далее минимум один символ: буквы или цифры
                regexp = "[A-Za-z][A-Za-z0-9]{1,}",
                message = "Product title should be at list thee characters length and start with capital letter")
        String model,

        @Schema(description = "Car build year", example = "2025")
        int year,

        @Schema(
                description = "Сar body type",
                example = "SEDAN")
        CarType type,

        @Schema(
                description = "Car fuel type",
                example = "PETROL")
        CarFuelType fuelType,

        @Schema(
                description = "Car transmission type",
                example = "AUTOMATIC")
        CarTransmissionType transmissionType,

        @Schema(
                description = "Car state (used / deleted)",
                example = "true")
        boolean isActive,

        @Schema(
                description = "Car status",
                example = "RENTED")
        String carStatus,

        @Schema(description = "Car day rental price", example = "150.00")
        @Positive(message = "Car rental price must be positive (more than zero)")
        BigDecimal dayRentalPrice,

        @Schema(
                description = "Car image",
                example = "https://shop-bucket.fra1.digitaloceanspaces.com/coconut-caf872c7-2ebd-4ec0-bd28-ff198091392c.png")
        String carImage) {

}
