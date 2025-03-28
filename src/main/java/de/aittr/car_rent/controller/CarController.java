package de.aittr.car_rent.controller;

import de.aittr.car_rent.domain.dto.CarResponseDto;
import de.aittr.car_rent.service.interfaces.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
@Tag(name = "Car controller", description = "Controller for various operations with cars")
public class CarController {

    private final CarService carService;

    // POST -> localhost:8080/cars
    @PostMapping
    @Operation(
            summary = "Save new car",
            description = "Saving new car into the database")
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN'})")
    @SecurityRequirement(name = "bearerAuth")
    public CarResponseDto saveCar(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Instance of a Car")
            CarResponseDto carDto) {
        return carService.saveCar(carDto);
    }

    // GET -> localhost:8080/cars
    @GetMapping
    @Operation(
            summary = "Get all cars",
            description = "Getting all cars that exist in the database")
    public List<CarResponseDto> getAllCars() {
        return carService.getAllCars();
    }

    // GET -> localhost:8080/cars/5
    @GetMapping("/{id}")
    @Operation(
            summary = "Get car by car id",
            description = "Getting car that exist in the database by car id")
    public CarResponseDto getCarById(
            @PathVariable
            @Parameter(description = "Car unique identifier")
            Long id) {
        return carService.getCarById(id);
    }

    // GET -> localhost:8080/cars/brand/toyota
    @GetMapping("/filter/brand/{brand}")
    @Operation(
            summary = "Get cars list by car brand",
            description = "Getting car that exist in the database by car brand")
    public List<CarResponseDto> getCarsByBrand(
            @PathVariable
            @Parameter(description = "Car brand title")
            String brand) {
        return carService.getCarsByBrand(brand);
    }

    // GET -> localhost:8080/cars/model/corolla
    @GetMapping("/filter/model/{model}")
    @Operation(
            summary = "Get cars list by car model",
            description = "Getting car that exist in the database by car model")
    public List<CarResponseDto> getCarsByModel(
            @PathVariable
            @Parameter(description = "Car model title")
            String model) {
        return carService.getCarsByModel(model);
    }

    // GET -> localhost:8080/cars/year/2006
    @GetMapping("/filter/year/{year}")
    @Operation(
            summary = "Get cars list by car year",
            description = "Getting car that exist in the database by car year")
    public List<CarResponseDto> getCarsByYear(
            @PathVariable
            @Parameter(description = "Car build year")
            int year) {
        return carService.getCarsByYear(year);
    }

    // GET -> localhost:8080/cars/type/sedan
    @GetMapping("/filter/type/{type}")
    @Operation(
            summary = "Get cars list y car type",
            description = "Getting car that exist in the database by car type")
    public List<CarResponseDto> getCarsByType(
            @PathVariable
            @Parameter(description = "Car type title")
            String type) {
        return carService.getCarsByType(type);
    }

    // GET -> localhost:8080/cars/fuel-type/petrol
    @GetMapping("/filter/fuel-type/{fuelType}")
    @Operation(
            summary = "Get cars list by car fuel type",
            description = "Getting car that exist in the database by car fuel type")
    public List<CarResponseDto> getCarsByFuelType(
            @PathVariable
            @Parameter(description = "Car fuel type title")
            String fuelType) {
        return carService.getCarsByFuelType(fuelType);
    }

    // GET -> localhost:8080/cars/transmission-type/manual
    @GetMapping("/filter/transmission-type/{transmissionType}")
    @Operation(
            summary = "Get cars list by car transmission type",
            description = "Getting car that exist in the database by car transmission type")
    public List<CarResponseDto> getCarsByTransmissionType(
            @PathVariable
            @Parameter(description = "Car transmission type title")
            String transmissionType) {
        return carService.getCarsByTransmissionType(transmissionType);
    }

    // GET -> localhost:8080/cars/available
    // GET -> localhost:8080/cars/car-status/under_repair
    @GetMapping("/filter/car-status/{carStatus}")
    @Operation(
            summary = "Get cars list by car status",
            description = "Getting car that exist in the database by car status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public List<CarResponseDto> getCarsByCarStatus(
            @PathVariable
            @Parameter(description = "Car status title")
            String carStatus) {
        return carService.getCarsByCarStatus(carStatus);
    }

    // GET -> localhost:8080/cars/rental-price/150-200
    @GetMapping("/filter/rental-price/{minDayRentalPrice}-{maxDayRentalPrice}")
    @Operation(
            summary = "Get cars list by min und max car day rental price",
            description = "Getting car that exist in the database by min und max car day rental price")
    public List<CarResponseDto> getCarsByDayRentalPrice(
            @PathVariable
            @Parameter(description = "Minimal day rental car price")
            BigDecimal minDayRentalPrice,

            @PathVariable
            @Parameter(description = "Maximal day rental car price")
            BigDecimal maxDayRentalPrice) {
        return carService.getCarsByDayRentalPrice(minDayRentalPrice, maxDayRentalPrice);
    }

    // GET -> localhost:8080/cars/time/2025-03-28T11:46-2025-03-31T08:58
    @GetMapping("/filter/time/{startDateTime}-{endDateTime}")
    @Operation(
            summary = "Get available cars list by start und end days",
            description = "Getting available cars list that exist in the database by start und end days")
    public List<CarResponseDto> getAllAvailableCarsByDates(
            @PathVariable
            @Parameter(description = "Start day")
            LocalDateTime startDateTime,

            @PathVariable
            @Parameter(description = "End day")
            LocalDateTime endDateTime) {
        return carService.getAllAvailableCarsByDates(startDateTime, endDateTime);
    }

    // PUT -> localhost:8080/cars  (идентификатор отправляется в теле запроса)
    @PutMapping
    @Operation(
            summary = "Update car",
            description = "Update car day rental price and car status in the database based on the passed CarResponseDto object"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public void updateCar(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Instance of a Car")
            CarResponseDto carDto) {
        carService.updateCar(carDto);
    }

    // DELETE -> localhost:8080/cars/3
    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Delete car by car id",
            description = "Change car property active in the database on false")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteCarById(@PathVariable Long id) {
        carService.deleteCarById(id);
    }

    // GET-> localhost:8080/api/cars/filter?startDateTime=2024-03-20T10:00:00&endDateTime=2024-03-25T10:00:00&minPrice=50&maxPrice=200
    @GetMapping("/filter")
    @Operation(
            summary = "Filter available cars",
            description = "Filter available cars by various criteria including dates, brand, fuel type, transmission type and price range")
    public List<CarResponseDto> filterAvailableCars(
            @RequestParam(required = false)
            @Parameter(description = "Start date and time of rental period")
            LocalDateTime startDateTime,

            @RequestParam(required = false)
            @Parameter(description = "End date and time of rental period")
            LocalDateTime endDateTime,

            @RequestParam(required = false)
            @Parameter(description = "Car brand")
            String brand,

            @RequestParam(required = false)
            @Parameter(description = "Car fuel type")
            String fuelType,

            @RequestParam(required = false)
            @Parameter(description = "Car transmission type")
            String transmissionType,

            @RequestParam(required = false)
            @Parameter(description = "Minimum rental price per day")
            BigDecimal minPrice,

            @RequestParam(required = false)
            @Parameter(description = "Maximum rental price per day")
            BigDecimal maxPrice) {
        return carService.filterAvailableCars(startDateTime, endDateTime, brand, fuelType, transmissionType, minPrice, maxPrice);
    }

    // GET -> localhost:8080/api/cars/brands
    @GetMapping("/brands")
    @Operation(
            summary = "Get all available car brands",
            description = "Get a list of all unique car brands that a currently active"
    )
    public List<String> getAllAvailableBrands() {
        return carService.getAllAvailableBrands();
    }
}
