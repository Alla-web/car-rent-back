package de.aittr.car_rent.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "{car.brand.notBlank}")
    @Column(name = "brand", nullable = false)
    @Pattern(
            regexp = "[A-Za-z ]{1,}",
            message = "Car brand should be at list two characters length and start with capital letter")
    @Size(max = 29, message = "Car brand must not exceed 19 characters")
    private String brand;

    @NotNull(message = "{car.model.notBlank}")
    @Column(name = "model", nullable = false)
    @Pattern(
            regexp = "([A-Z0-9]{1,}[a-zA-Z0-9]*)(\\s+([A-Z0-9]{1,}[a-zA-Z0-9]*))*",
            message = "Car model title must start with a letter or digit and may contain letters, digits, and spaces. Each word must start with a letter or digit")
    @Size(max = 14, message = "Car model must not exceed 14 characters")
    private String model;

    @NotNull(message = "{car.year.notBlank}")
    @Column(name = "year", nullable = false)
    @Max(3000)
    @Min(1600)
    private int year;

    @NotNull(message = "{car.type.notBlank}")
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarType type;

    @NotNull(message = "{car.fuel_type.notBlank}")
    @Column(name = "fuel_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarFuelType fuelType;

    @NotNull(message = "{car.transmission_type.notBlank}")
    @Column(name = "transmission_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarTransmissionType transmissionType;

    @NotNull(message = "{car.active.notBlank}")
    @Column(name = "active", nullable = false)
    private boolean isActive;

    @NotNull(message = "{car.car_status.notBlank}")
    @Column(name = "car_status")
    @Enumerated(EnumType.STRING)
    private CarStatus carStatus;

    @NotNull(message = "{car.day_rental_price.notBlank}")
    @Column(name = "day_rental_price", nullable = false)
    @Positive(message = "Car rental price must be positive (more than zero)")
    @DecimalMax(value = "5000.00", message = "Car rental day price must not exceed 5000")
    private BigDecimal dayRentalPrice;

    @Column(name = "car_image")
    private String carImage;

    public Car(String brand,
               String model,
               int year,
               CarType type,
               CarFuelType fuelType,
               CarTransmissionType transmissionType,
               BigDecimal dayRentalPrice) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.type = type;
        this.fuelType = fuelType;
        this.transmissionType = transmissionType;
        this.dayRentalPrice = dayRentalPrice;
        this.carStatus=CarStatus.AVAILABLE;
        this.isActive = true;
    }

    @Override
    public String toString() {
        return String.format("Car: id - %d; brand - %s; model - %s; year - %d; type - %s; fuel type - %s, transmission type - %s; car status - %s; day rental price - %.2f; active - %s",
                id, brand, model, year, type, fuelType, transmissionType, carStatus, dayRentalPrice, isActive ? "yes" : "no");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
