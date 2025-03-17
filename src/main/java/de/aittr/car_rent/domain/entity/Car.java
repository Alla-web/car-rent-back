package de.aittr.car_rent.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private String brand;

    @NotNull(message = "{model.notBlank}")
    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "fuel_type", nullable = false)
    private String fuelType;

    @Column(name = "transmission_type", nullable = false)
    private String transmissionType;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "car_status")
    private String carStatus;

    @Column(name = "day_rental_price", nullable = false)
    private BigDecimal dayRentalPrice;

    @Column(name = "car_image")
    private String carImage;

    public Car(String brand,
               String model,
               int year,
               String type,
               String fuelType,
               String transmissionType,
               BigDecimal dayRentalPrice) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.type = type;
        this.fuelType = fuelType;
        this.transmissionType = transmissionType;
        this.dayRentalPrice = dayRentalPrice;
        this.active = true;
    }

    @Override
    public String toString() {
        return String.format("Car: id - %d; brand - %s; model - %s; year - %d; type - %s; fuel type - %s, transmission type - %s; car status - %s; day rental price - %.2f; active - %s",
                id, brand, model, year, type, fuelType, transmissionType, carStatus, dayRentalPrice, active ? "yes" : "no");
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
