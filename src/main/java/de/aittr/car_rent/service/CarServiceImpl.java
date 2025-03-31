package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.dto.CarResponseDto;
import de.aittr.car_rent.domain.entity.*;
import de.aittr.car_rent.exception_handling.exceptions.CarNotFoundException;
import de.aittr.car_rent.repository.BookingRepository;
import de.aittr.car_rent.repository.CarRepository;
import de.aittr.car_rent.service.interfaces.CarService;
import de.aittr.car_rent.service.mapping.CarMappingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMappingService carMappingService;
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
                .filter((car -> car.getCarStatus() == CarStatus.RENTED || car.getCarStatus() == CarStatus.AVAILABLE))
                .sorted(Comparator.comparing(Car::getType))
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
    public List<CarResponseDto> getCarsByType(CarType type) {
        return carRepository.findAll()
                .stream()
                .filter(car -> car.getType().equals(type))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByFuelType(CarFuelType fuelType) {
        return carRepository.findAll()
                .stream()
                .filter(car -> car.getFuelType().equals(fuelType))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByTransmissionType(CarTransmissionType transmissionType) {
        return carRepository.findAll()
                .stream()
                .filter(car -> car.getTransmissionType().equals(transmissionType))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByCarStatus(CarStatus carStatus) {
        return carRepository.findAll()
                .stream()
                .filter(car -> Objects.nonNull(car.getCarStatus()) && car.getCarStatus().equals(carStatus))
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
                .orElseThrow(() -> new CarNotFoundException(id))
                .setCarImage(imageUrl);
    }

    @Override
    @Transactional
    public List<CarResponseDto> getAllAvailableCarsByDates(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime) {

        return carRepository.findAll()
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

    @Override
    @Transactional
    public List<CarResponseDto> filterAvailableCars(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String brand,
            String fuelType,
            String transmissionType,
            BigDecimal minPrice,
            BigDecimal maxPrice) {
        return getAllAvailableCarsByDates(startDateTime, endDateTime)
                .stream()
                .filter(car -> brand == null || car.brand().equalsIgnoreCase(brand.trim()))
                .filter(car -> fuelType == null || car.fuelType().name().equalsIgnoreCase(fuelType.trim()))
                .filter(car -> transmissionType == null || car.transmissionType().name().equalsIgnoreCase(transmissionType.trim()))
                .filter(car -> minPrice == null || car.dayRentalPrice().compareTo(minPrice) >= 0)
                .filter(car -> maxPrice == null || car.dayRentalPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllAvailableBrands() {
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .map(Car::getBrand)
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    @SneakyThrows
    public String attachImageToCar(Long id, MultipartFile file) {
        log.info("Start uploading image for car ID: {}", id);

        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        String fileExtension = getFileExtension(file.getOriginalFilename());
        if (!"jpg".equalsIgnoreCase(fileExtension) && !"png".equalsIgnoreCase(fileExtension) && !"jpeg".equalsIgnoreCase(fileExtension)) {
            throw new IllegalArgumentException("Only jpg, png, and jpeg images are allowed");
        }

        String fileName = id + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads/cars", fileName);

        Files.createDirectories(path.getParent());
        file.transferTo(path);

        String imageUrl = "/uploads/cars/" + fileName;
        car.setCarImage(imageUrl);
        carRepository.save(car);

        log.info("Image uploaded successfully for car ID: {}", id);
        return imageUrl;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
    }
}

