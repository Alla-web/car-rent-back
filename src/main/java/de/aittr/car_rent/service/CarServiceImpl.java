package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.dto.CarResponseDto;
import de.aittr.car_rent.domain.entity.*;
import de.aittr.car_rent.exception_handling.exceptions.CarNotFoundException;
import de.aittr.car_rent.exception_handling.exceptions.RestApiException;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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
        if (carDto == null) {
            throw new RestApiException("Received no information about car");
        }
        Car entity = carMappingService.mapDtoToEntity(carDto);
        entity = carRepository.save(entity);
        return carMappingService.mapEntityToDto(entity);
    }

    @Override
    public List<CarResponseDto> getAllCars() {
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter((car -> car.getCarStatus() == CarStatus.RENTED ||
                        car.getCarStatus() == CarStatus.AVAILABLE ||
                        car.getCarStatus() == CarStatus.UNDER_INSPECTION))
                .sorted(Comparator.comparing(Car::getType))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getAllCarsToAdmin() {
        return carRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Car::isActive)
                        .thenComparing(Car::getCarStatus)
                        .thenComparing(Car::getType)
                        .thenComparing(Car::getTransmissionType)
                        .thenComparing(Car::getBrand)
                        .thenComparing(Car::getYear))
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
                .filter(Car::isActive)
                .filter(car -> car.getBrand().equalsIgnoreCase(brand.trim()))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByModel(String model) {
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter(car -> car.getModel().equalsIgnoreCase(model.trim()))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByYear(int year) {
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter(car -> car.getYear() == year)
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByType(CarType type) {
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter(car -> car.getType().equals(type))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByFuelType(CarFuelType fuelType) {
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter(car -> car.getFuelType().equals(fuelType))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByTransmissionType(CarTransmissionType transmissionType) {
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter(car -> car.getTransmissionType().equals(transmissionType))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByCarStatus(CarStatus carStatus) {
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter(car -> Objects.nonNull(car.getCarStatus()) && car.getCarStatus().equals(carStatus))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByDayRentalPrice(BigDecimal minDayRentalPrice, BigDecimal maxDayRentalPrice) {
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter(car -> car.getDayRentalPrice().compareTo(minDayRentalPrice) >= 0 &&
                        car.getDayRentalPrice().compareTo(maxDayRentalPrice) <= 0)
                .sorted(Comparator.comparing(Car::getDayRentalPrice))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public boolean checkIfCarAvailableByDates(
            Long carId,
            LocalDateTime from,
            LocalDateTime to) {
        return bookingRepository.findAllByCarId(carId)
                .stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.ACTIVE)
                .noneMatch(booking ->
                        booking.getRentalStartDate().isBefore(to) &&
                                booking.getRentalEndDate().isAfter(from)
                );
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
    public CarResponseDto deleteCarById(Long id) {
        Car existingCar = getOrThrow(id);
        existingCar.setActive(false);
        carRepository.save(existingCar);
        return carMappingService.mapEntityToDto(existingCar);
    }

    @Transactional
    @Override
    public CarResponseDto restoreCar(Long id) {
        Car restoredCar = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car with id " + id + " not found"));
        if (restoredCar.isActive()) {
            throw new RestApiException("Car with id " + id + " is already active");
        }
        restoredCar.setActive(true);
        carRepository.save(restoredCar);
        return carMappingService.mapEntityToDto(restoredCar);
    }


    @Override
    @Transactional
    public List<CarResponseDto> getAllAvailableCarsByDates(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            throw new RestApiException("Start and end dates cannot be null");
        }
        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new RestApiException("Start date and time must be today or in the future");
        }
        if (endDateTime.isBefore(LocalDateTime.now())) {
            throw new RestApiException("End date and time must be after the start day and time");
        }
        return carRepository.findAll()
                .stream()
                .filter(car -> {
                    List<Booking> bookings = bookingRepository.findAllByCarId(car.getId());
                    if (bookings == null || bookings.isEmpty()) {
                        return true;
                    }
                    return bookings.stream()
                            .noneMatch(booking ->
                                    booking.getRentalStartDate().isBefore(endDateTime) &&
                                            booking.getRentalEndDate().isAfter(startDateTime)
                            );
                })
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> filterAvailableCars(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            List<String> brands,
            List<String> fuelTypes,
            List<String> transmissionTypes,
            BigDecimal minPrice,
            BigDecimal maxPrice) {
        if (startDateTime == null || endDateTime == null) {
            throw new RestApiException("Start and end dates cannot be null");
        }
        if (startDateTime.toLocalDate().isBefore(LocalDate.now())) {
            throw new RestApiException("Start date must be today or in the future");
        }
        if (endDateTime.isBefore(LocalDateTime.now())) {
            throw new RestApiException("Start date must be today or in the future");
        }
        return getAllAvailableCarsByDates(startDateTime, endDateTime)
                .stream()
                .filter(car ->
                        brands == null || brands.isEmpty() ||
                                brands.stream().anyMatch(brand ->
                                        brand.equalsIgnoreCase(car.brand())))
                .filter(car ->
                        fuelTypes == null || fuelTypes.isEmpty() ||
                                fuelTypes.stream().anyMatch(fuelType ->
                                        fuelType.equalsIgnoreCase(car.fuelType().name())))
                .filter(car ->
                        transmissionTypes == null || transmissionTypes.isEmpty() ||
                                transmissionTypes.stream().anyMatch(transmissionType ->
                                        transmissionType.equalsIgnoreCase(car.transmissionType().name())))
                .filter(car ->
                        (minPrice == null || car.dayRentalPrice().compareTo(minPrice) >= 0) &&
                                (maxPrice == null || car.dayRentalPrice().compareTo(maxPrice) <= 0))
                .toList();
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

    @Override
    public List<String> getAllCarTypes() {
        return Arrays.stream(CarType.values())
                .map(Enum::name)
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

