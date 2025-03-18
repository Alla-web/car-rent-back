package de.aittr.car_rent.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.beans.BeanInfo;
import java.util.Objects;

@Getter
@Setter
@Schema(description = "Class that describes Car DTO")
public class CarDto {

    @Schema(
            description = "Car unique identifier",
            example = "123",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(description = "Car brand", example = "VW")
    private String brand;

    @Schema(description = "Car model", example = "Golf")
    private String model;

    @Schema(description = "Car build year", example = "2025")
    private int year;

    @Schema(description = "Сar body type", example = "Sedan")
    private String type;

    @Schema(description = "Car fuel type", example = "Petrol")
    private String fuelType;

    @Schema(description = "Car transmission type", example = "Automatic")
    private String transmissionType;

    @Schema(description = "Car status", example = "Rented")
    private String carStatus;

    @Schema(description = "Car day rental price", example = "150.00")
    private BeanInfo dayRentalPrice;

    @Schema(description = "Car image", example = "https://shop-bucket.fra1.digitaloceanspaces.com/coconut-caf872c7-2ebd-4ec0-bd28-ff198091392c.png")
    private String carImage;


    @Override
    public String toString() {
        return String.format("Car: id - %d; brand - %s; model - %s; year - %d; type - %s; fuel type - %s, transmission type - %s; car status - %s; day rental price - %.2f",
                id, brand, model, year, type, fuelType, transmissionType, carStatus, dayRentalPrice);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CarDto carDto = (CarDto) o;
        return Objects.equals(id, carDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
