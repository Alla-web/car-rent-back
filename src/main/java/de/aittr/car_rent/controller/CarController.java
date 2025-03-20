package de.aittr.car_rent.controller;

import de.aittr.car_rent.domain.dto.CarResponseDto;
import de.aittr.car_rent.service.interfaces.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

private final CarService carService;

    // POST -> http://IP-adress:8080/cars
    @PostMapping
    public CarResponseDto saveCar(@RequestBody CarResponseDto carDto) {
        return carService.saveCar(carDto);
    }

    public List<CarResponseDto> getAllCars() {
        return carService.getAllCars();
    }

    public CarResponseDto getCarById(Long id){
        return carService.getCarById(id);
    }

    public List<CarResponseDto> getCarsByBrand(String brand) {
        return carService.getCarsByBrand(brand);
    }

    public List<CarResponseDto> getCarsByModel(String model) {
        return carService.getCarsByModel(model);
    }

    public List<CarResponseDto> getCarsByYear(int year) {
        return carService.getCarsByYear(year);
    }

    public List<CarResponseDto> getCarsByType(String type) {
        return carService.getCarsByType(type);
    }

    public List<CarResponseDto> getCarsByFuelType(String fuelType) {
        return carService.getCarsByFuelType(fuelType);
    }

    public List<CarResponseDto> getCarsByTransmissionType(String transmissionType) {
        return carService.getCarsByTransmissionType(transmissionType);
    }

    public List<CarResponseDto> getCarsByCarStatus(String carStatus) {
        return carService.getCarsByCarStatus(carStatus);
    }

    public List<CarResponseDto> getCarsByDayRentalPrice(BigDecimal minDayRentalPrice, BigDecimal maxDayRentalPrice) {
        return carService.getCarsByDayRentalPrice(minDayRentalPrice, maxDayRentalPrice);
    }

    public void updateCar(CarResponseDto carDto) {
        carService.updateCar(carDto);
    }

    public void deleteCarById(Long id) {
        carService.deleteCarById(id);
    }

    public void attachImageToCar(Long id, String imageUrl) {
        carService.attachImageToCar(id, imageUrl);
    }

}
