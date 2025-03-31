package de.aittr.car_rent.controller;

import de.aittr.car_rent.domain.dto.CarResponseDto;
import de.aittr.car_rent.domain.entity.CarFuelType;
import de.aittr.car_rent.domain.entity.CarStatus;
import de.aittr.car_rent.domain.entity.CarTransmissionType;
import de.aittr.car_rent.domain.entity.CarType;
import de.aittr.car_rent.service.interfaces.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
@Tag(name = "Car controller", description = "Controller for various operations with cars")
public class CarController {

    private final CarService carService;

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

    @GetMapping
    @Operation(
            summary = "Get all cars",
            description = "Getting all cars that exist in the database")
    public List<CarResponseDto> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get car by car id",
            description = "Getting car that exist in the database by car id")
    public CarResponseDto getCarById(
            @PathVariable
            @Parameter(description = "Car unique identifier", example = "7")
            Long id) {
        return carService.getCarById(id);
    }

    @GetMapping("/filter/brand")
    @Operation(
            summary = "Get cars list by car brand",
            description = "Getting car that exist in the database by car brand")
    public List<CarResponseDto> getCarsByBrand(
            @RequestParam("brand")
            @Parameter(description = "Car brand title")
            String brand) {
        return carService.getCarsByBrand(brand);
    }

    @GetMapping("/filter/model")
    @Operation(
            summary = "Get cars list by car model",
            description = "Getting car that exist in the database by car model")
    public List<CarResponseDto> getCarsByModel(
            @RequestParam("model")
            @Parameter(description = "Car model title", example = "Corolla")
            String model) {
        return carService.getCarsByModel(model);
    }

    @GetMapping("/filter/year")
    @Operation(
            summary = "Get cars list by car year",
            description = "Getting car that exist in the database by car year")
    public List<CarResponseDto> getCarsByYear(
            @RequestParam("year")
            @Parameter(description = "Car build year", example = "2020")
            int year) {
        return carService.getCarsByYear(year);
    }

    @GetMapping("/filter/type")
    @Operation(
            summary = "Get cars list y car type",
            description = "Getting car that exist in the database by car type")
    public List<CarResponseDto> getCarsByType(
            @RequestParam("type")
            @Parameter(description = "Car type title")
            CarType type) {
        return carService.getCarsByType(type);
    }

    @GetMapping("/filter/fuel-type")
    @Operation(
            summary = "Get cars list by car fuel type",
            description = "Getting car that exist in the database by car fuel type")
    public List<CarResponseDto> getCarsByFuelType(
            @RequestParam("fuel-type")
            @Parameter(description = "Car fuel type title", example = "PETROL")
            CarFuelType fuelType) {
        return carService.getCarsByFuelType(fuelType);
    }

    @GetMapping("/filter/transmission-type")
    @Operation(
            summary = "Get cars list by car transmission type",
            description = "Getting car that exist in the database by car transmission type")
    public List<CarResponseDto> getCarsByTransmissionType(
            @RequestParam("transmission-type")
            @Parameter(description = "Car transmission type title", example = "SEDAN")
            CarTransmissionType transmissionType) {
        return carService.getCarsByTransmissionType(transmissionType);
    }

    @GetMapping("/filter/car-status")
    @Operation(
            summary = "Get cars list by car status",
            description = "Getting car that exist in the database by car status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public List<CarResponseDto> getCarsByCarStatus(
            @RequestParam("car-status")
            @Parameter(description = "Car status", example = "AVAILABLE")
            CarStatus carStatus) {
        return carService.getCarsByCarStatus(carStatus);
    }

    @GetMapping("/filter/rental-price")
    @Operation(
            summary = "Get cars list by min und max car day rental price",
            description = "Getting car that exist in the database by min und max car day rental price")
    public List<CarResponseDto> getCarsByDayRentalPrice(
            @RequestParam("from")
            @Parameter(description = "Minimal day rental car price", example = "50")
            BigDecimal minDayRentalPrice,

            @RequestParam("to")
            @Parameter(description = "Maximal day rental car price", example = "150")
            BigDecimal maxDayRentalPrice) {
        return carService.getCarsByDayRentalPrice(minDayRentalPrice, maxDayRentalPrice);
    }

    @GetMapping("/available-car")
    @Operation(
            summary = "Checks if the car is available during the period",
            description = "Checks whether the car is available during the period for extending a specific booking or not")
    public boolean checkIfCarAvailableByDates(
            @RequestParam(value = "booking-id", required = false)
            @Parameter(description = "Booking unique identifier", example = "7")
            Long bookingId,

            @RequestParam("car-id")
            @Parameter(description = "Car unique identifier", example = "7")
            Long carId,

            @RequestParam("from")
            @Parameter(description = "Period start day", example = "2025-04-01T10:00")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @RequestParam("to")
            @Parameter(description = "Period end day", example = "2025-04-05T11:00")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to) {
        return carService.checkIfCarAvailableByDates(carId, from, to);
    }

    @GetMapping("/filter/time")
    @Operation(
            summary = "Get available cars list by start und end days",
            description = "Getting available cars list that exist in the database by start und end days")
    public List<CarResponseDto> getAllAvailableCarsByDates(
            @RequestParam(value = "from")
            @Parameter(description = "Start day", example = "2025-03-28T11:46")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDateTime,

            @RequestParam(value = "to")
            @Parameter(description = "End day", example = "2025-03-30T11:46")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDateTime) {
        return carService.getAllAvailableCarsByDates(startDateTime, endDateTime);
    }

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

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Delete car by car id",
            description = "Change car property active in the database on false")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteCarById(@PathVariable Long id) {
        carService.deleteCarById(id);
    }

    //    GET-> localhost:8080/api/cars/filter?startDateTime=2024-03-20T10:00:00&endDateTime=2024-03-25T10:00:00&minPrice=50&maxPrice=200
    @GetMapping("/filter")
    @Operation(
            summary = "Filter available cars",
            description = "Filter available cars by various criteria including dates, brand, fuel type, transmission type and price range")
    public List<CarResponseDto> filterAvailableCars(
            @RequestParam(required = false)
            @Parameter(description = "Start date and time of rental period", example = "2025-03-28T00:00")
            LocalDateTime startDateTime,

            @RequestParam(required = false)
            @Parameter(description = "End date and time of rental period", example = "2025-03-28T00:00")
            LocalDateTime endDateTime,

            @RequestParam(required = false)
            @Parameter(description = "Car brand", example = "Toyota")
            String brand,

            @RequestParam(required = false)
            @Parameter(description = "Car fuel type", example = "Corolla")
            String fuelType,

            @RequestParam(required = false)
            @Parameter(description = "Car transmission type", example = "MANUAL")
            String transmissionType,

            @RequestParam(required = false)
            @Parameter(description = "Minimum rental price per day", example = "50")
            BigDecimal minPrice,

            @RequestParam(required = false)
            @Parameter(description = "Maximum rental price per day", example = "150")
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

    @PostMapping("/upload-image")
    @Operation(
            summary = "Attach image to car",
            description = "Attaches a link to a photo stored locally on the computer to the car in the database")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> uploadCarImage(
            @RequestParam("id")
            @Parameter(description = "Car unique identifier", example = "15")
            Long carId,
            @RequestParam("file")
            @Parameter(description = "Car image", example = "https://shop-bucket.fra1.digitaloceanspaces.com/coconut-caf872c7-2ebd-4ec0-bd28-ff198091392c.png")
            MultipartFile file) {
        try {

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("The file must not be empty");
            }

            String imageUrl;
            imageUrl = carService.attachImageToCar(carId, file);

            return ResponseEntity.ok("Image uploaded successfully. URL: " + imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error loading image");
        }
    }
}
