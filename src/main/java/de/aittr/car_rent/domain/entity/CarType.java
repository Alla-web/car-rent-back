package de.aittr.car_rent.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Car body type",
        example = "SEDAN"
)
public enum CarType {

    SEDAN,
    HATCHBACK,
    COUPE,
    CONVERTIBLE,
    SUV,
    CROSSOVER,
    PICKUP,
    MINIVAN,
    WAGON,
    ROADSTER,
    CABRIOLET,
    LIMOUSINE,
    VAN,
    TRUCK,
    JEEP,
}
