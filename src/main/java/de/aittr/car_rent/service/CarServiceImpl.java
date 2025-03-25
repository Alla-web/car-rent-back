package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.dto.CarResponseDto;
import de.aittr.car_rent.domain.entity.Car;
import de.aittr.car_rent.domain.entity.CarStatus;
import de.aittr.car_rent.exception_handling.exceptions.CarNotFoundException;
import de.aittr.car_rent.repository.BookingRepository;
import de.aittr.car_rent.repository.CarRepository;
import de.aittr.car_rent.service.interfaces.CarService;
import de.aittr.car_rent.service.mapping.CarMappingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMappingService carMappingService;
    private final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);
    private final BookingRepository bookingRepository;

    @Override
    public CarResponseDto saveCar(CarResponseDto carDto) {
        Car entity = carMappingService.mapDtoToEntity(carDto);
        entity = carRepository.save(entity);
        return carMappingService.mapEntityToDto(entity);
    }

    @Override
    public List<CarResponseDto> getAllCars() {
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public CarResponseDto getCarById(Long id) {
        return carMappingService.mapEntityToDto(getOrThrow(id));
    }

    @Transactional
    @Override
    public Car getOrThrow(Long id) {
        return carRepository.findById(id).orElseThrow(() -> new CarNotFoundException(id));
    }

    @Override
    public List<CarResponseDto> getCarsByBrand(String brand) {
        return carRepository.findAll()
                .stream()
                .filter(car -> car.getBrand().equalsIgnoreCase(brand.trim()))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByModel(String model) {
        return carRepository.findAll()
                .stream()
                .filter(car -> car.getModel().equalsIgnoreCase(model.trim()))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByYear(int year) {
        return carRepository.findAll()
                .stream()
                .filter(car -> car.getYear() == year)
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByType(String type) {
        return carRepository.findAll()
                .stream()
                .filter(car -> car.getType().name().equalsIgnoreCase(type.trim()))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByFuelType(String fuelType) {
        return carRepository.findAll()
                .stream()
                .filter(car -> car.getFuelType().name().equalsIgnoreCase(fuelType.trim()))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByTransmissionType(String transmissionType) {
        return carRepository.findAll()
                .stream()
                .filter(car -> car.getTransmissionType().name().equalsIgnoreCase(transmissionType.trim()))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByCarStatus(String carStatus) {
        return carRepository.findAll()
                .stream()
                .filter(car -> Objects.nonNull(car.getCarStatus()) && car.getCarStatus().name().equalsIgnoreCase(carStatus.trim()))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByDayRentalPrice(BigDecimal minDayRentalPrice, BigDecimal maxDayRentalPrice) {
        return carRepository.findAll()
                .stream()
                .filter(car -> car.getDayRentalPrice().compareTo(minDayRentalPrice) >= 0 &&
                        car.getDayRentalPrice().compareTo(maxDayRentalPrice) <= 0)
                .sorted(Comparator.comparing(Car::getDayRentalPrice))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateCar(CarResponseDto carDto) {
        Long id = carDto.id();
        Car existCar = getOrThrow(id);
        //TODO нужно ли ещё что-то обновлять?
        existCar.setDayRentalPrice(carDto.dayRentalPrice());
        existCar.setCarStatus(CarStatus.valueOf(carDto.carStatus()));
    }

    @Override
    @Transactional
    public void deleteCarById(Long id) {
        Car existingCar = getOrThrow(id);
        existingCar.setActive(false);
        carRepository.save(existingCar);
    }

    @Override
    @Transactional
    public void attachImageToCar(Long id, String imageUrl) {
        carRepository.findById(id)
                .orElseThrow(()->new CarNotFoundException(id))
                .setCarImage(imageUrl);

    }

    @Override
    @Transactional
    public List<CarResponseDto> getAllAvailableCarsByDates(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return
                carRepository.findAll()
                        .stream()
                        .filter((car -> car.isActive() && car.getCarStatus() == CarStatus.AVAILABLE))
                        .filter(car -> bookingRepository.findAllByCarId(car.getId()).stream()
                                .noneMatch(booking ->
                                        booking.getRentalStartDate().isBefore(endDateTime) &&
                                                booking.getRentalEndDate().isAfter(startDateTime)
                                )
                        )
                        .map(carMappingService::mapEntityToDto)
                        .collect(Collectors.toList());
    }
}

