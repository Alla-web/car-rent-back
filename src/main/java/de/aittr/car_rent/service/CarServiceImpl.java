package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.dto.CarResponseDto;
import de.aittr.car_rent.domain.dto.CarUpdateRequestDto;
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
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMappingService carMappingService;
    private final BookingRepository bookingRepository;
    private final CarImageService carImageService;

    @Override
    public CarResponseDto saveCar(CarResponseDto carDto) {
        if (carDto == null) {
            throw new RestApiException("Received no information about car");
        }
        if (carDto.brand() == null || carDto.brand().isBlank()) {
            throw new RestApiException("Car brand must not be not blank");
        }
        if (carDto.model() == null || carDto.model().isBlank()) {
            throw new RestApiException("Car model must not be not blank");
        }
        if (carDto.year() < 1600 || carDto.year() > 3000) {
            throw new RestApiException("Car year must not be not blank and more than 1600 and less than 3000");
        }
        if (carDto.type() == null) {
            throw new RestApiException("Car type must not be not blank");
        }
        if (carDto.fuelType() == null) {
            throw new RestApiException("Fuel type must be not blank");
        }
        if (carDto.transmissionType() == null) {
            throw new RestApiException("Transmission type must be not blank");
        }
        if (carDto.dayRentalPrice() == null || carDto.dayRentalPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RestApiException("Rental price must be greater than zero");
        }
        Car entity = carMappingService.mapDtoToEntity(carDto);
        entity = carRepository.save(entity);
        return carMappingService.mapEntityToDto(entity);
    }

    @Override
    public List<CarResponseDto> getAllCars() {
        List<Car> carList = carRepository.findAll();
        if (carList.isEmpty()) {
            throw new RestApiException("No cars found");
        }
        Set<CarStatus> allowedCarStatuses = Set.of(
                CarStatus.RENTED,
                CarStatus.AVAILABLE,
                CarStatus.UNDER_INSPECTION
        );
        return carList.stream()
                .filter(Car::isActive)
                .filter(car -> car.getCarStatus() != null)
                .filter((car -> allowedCarStatuses.contains(car.getCarStatus())))
                .sorted(Comparator.comparing(Car::getType))
                .map(carMappingService::mapEntityToDto)
                .toList();

    }

    @Override
    public List<CarResponseDto> getAllCarsToAdmin() {
        List<Car> carList = carRepository.findAll();
        if (carList.isEmpty()) {
            throw new RestApiException("No cars found");
        }
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
        if (id == null) {
            throw new RestApiException("Enter car id");
        }
        return carMappingService.mapEntityToDto(getOrThrow(id));
    }

    @Transactional
    @Override
    public Car getOrThrow(Long id) {
        if (id == null) {
            throw new RestApiException("Enter car id");
        }
        return carRepository.findById(id).orElseThrow(() -> new CarNotFoundException(id));
    }

    @Override
    public List<CarResponseDto> getCarsByBrand(String brand) {
        List<Car> carList = carRepository.findAll();
        if (carList.isEmpty()) {
            throw new RestApiException("No cars found");
        }
        if (brand == null || brand.trim().isBlank() || brand.isEmpty()) {
            throw new RestApiException("Enter car brand");
        }
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter(car -> car.getBrand().equalsIgnoreCase(brand.trim()))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByModel(String model) {
        List<Car> carList = carRepository.findAll();
        if (carList.isEmpty()) {
            throw new RestApiException("No cars found");
        }
        if (model == null || model.trim().isBlank() || model.isEmpty()) {
            throw new RestApiException("Enter car model");
        }
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter(car -> car.getModel().equalsIgnoreCase(model.trim()))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByYear(int year) {
        List<Car> carList = carRepository.findAll();
        if (carList.isEmpty()) {
            throw new RestApiException("No cars found");
        }
        int currentYear = Year.now().getValue();
        if (year > currentYear) {
            throw new RestApiException("Year must be in the past");
        }
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter(car -> car.getYear() == year)
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByType(CarType type) {
        List<Car> carList = carRepository.findAll();
        if (carList.isEmpty()) {
            throw new RestApiException("No cars found");
        }
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter(car -> car.getType().equals(type))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByFuelType(CarFuelType fuelType) {
        List<Car> carList = carRepository.findAll();
        if (carList.isEmpty()) {
            throw new RestApiException("No cars found");
        }
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter(car -> car.getFuelType().equals(fuelType))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByTransmissionType(CarTransmissionType transmissionType) {
        List<Car> carList = carRepository.findAll();
        if (carList.isEmpty()) {
            throw new RestApiException("No cars found");
        }
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter(car -> car.getTransmissionType().equals(transmissionType))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByCarStatus(CarStatus carStatus) {
        List<Car> carList = carRepository.findAll();
        if (carList.isEmpty()) {
            throw new RestApiException("No cars found");
        }
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .filter(car -> Objects.nonNull(car.getCarStatus()) && car.getCarStatus().equals(carStatus))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByDayRentalPrice(BigDecimal minDayRentalPrice, BigDecimal maxDayRentalPrice) {
        List<Car> carList = carRepository.findAll();
        if (carList.isEmpty()) {
            throw new RestApiException("No cars found");
        }
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
        Car checkingCar = getOrThrow(carId);
        if(checkingCar.getCarStatus() == CarStatus.UNDER_REPAIR){
            throw new RestApiException("Car with id " + checkingCar.getId() + " is currently under repair. Please choose another car");
        }
        if(checkingCar.getCarStatus() == CarStatus.REMOVER_FROM_RENT){
            throw new RestApiException("Car with id " + checkingCar.getId() + " is currently deleted from renting. Please choose another car");
        }
        if(checkingCar.getCarStatus() == CarStatus.DELETED){
          throw new RestApiException("Car with id " + checkingCar.getId() + " is currently out of stock for renting. Please choose another car");
        }
        return bookingRepository.findAllByCarId(carId)
                .stream()
                .filter(booking ->
                        booking.getBookingStatus() == BookingStatus.PENDING ||
                        booking.getBookingStatus() == BookingStatus.ACTIVE)
                .noneMatch(booking ->
                        booking.getRentalStartDate().isBefore(to) &&
                                booking.getRentalEndDate().isAfter(from));
    }

    @Override
    @Transactional
    public CarResponseDto updateCar(CarUpdateRequestDto carDto, Long carId) {
        if (carId == null) {
            throw new RestApiException("Enter car id");
        }
        Car existCar = getOrThrow(carId);
        //TODO нужно ли ещё что-то обновлять?
        existCar.setDayRentalPrice(carDto.dayRentalPrice());
        existCar.setCarStatus(CarStatus.valueOf(carDto.carStatus()));
        carRepository.save(existCar);
        return carMappingService.mapEntityToDto(existCar);
    }

    @Override
    @Transactional
    public CarResponseDto deleteCarById(Long id) {
        if (id == null) {
            throw new RestApiException("Enter car id");
        }
        Car existingCar = getOrThrow(id);
        List<Booking> carBookingdList = bookingRepository.findAllByCarId(id)
                .stream()
                .filter(booking -> List.of(BookingStatus.ACTIVE, BookingStatus.PENDING)
                        .contains(booking.getBookingStatus()))
                .toList();
        if(!carBookingdList.isEmpty()) {
            throw new RestApiException("Car id " + id + " has opened bookings");
        }
        existingCar.setActive(false);
        existingCar.setCarStatus(CarStatus.DELETED);
        carRepository.save(existingCar);
        return carMappingService.mapEntityToDto(existingCar);
    }

    @Transactional
    @Override
    public CarResponseDto restoreCar(Long id) {
        if (id == null) {
            throw new RestApiException("Enter car id");
        }
        Car restoredCar = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car with id " + id + " not found"));
        if (restoredCar.isActive()) {
            throw new RestApiException("Car with id " + id + " is already active");
        }
        restoredCar.setActive(true);
        restoredCar.setCarStatus(CarStatus.AVAILABLE);
        carRepository.save(restoredCar);
        return carMappingService.mapEntityToDto(restoredCar);
    }

    @Override
    @Transactional
    public List<CarResponseDto> getAllAvailableCarsByDates(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        List<Car> carList = carRepository.findAll();
        if (carList.isEmpty()) {
            throw new RestApiException("No cars found");
        }
        if (startDateTime == null || endDateTime == null) {
            throw new RestApiException("Start and end dates cannot be null");
        }
        // Округляем время начала до следующей целой минуты
        LocalDateTime roundedStartDateTime = startDateTime
                .withSecond(0)
                .withNano(0)
                .plusMinutes(1);

        if (roundedStartDateTime.isBefore(LocalDateTime.now())) {
            throw new RestApiException("Start date and time must be in the future");
        }
        if (endDateTime.isBefore(roundedStartDateTime)) {
            throw new RestApiException("End date and time must be after the start date and time");
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
                                            booking.getRentalEndDate().isAfter(roundedStartDateTime)
                            );
                })
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> filterAvailableCars(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            List<String> brand,
            List<String> type,
            List<String> fuel,
            List<String> transmissionType,
            BigDecimal minPrice,
            BigDecimal maxPrice) {
        //log.info("Start filtering available cars from [{}] to [{}]", startDateTime, endDateTime);
        if (startDateTime == null || endDateTime == null) {
            //log.warn("❗️ Start or end date is null");
            throw new RestApiException("Start and end dates cannot be null");
        }
        // Округляем время начала до следующей целой минуты
        LocalDateTime roundedStartDateTime = startDateTime
                .withSecond(0)
                .withNano(0)
                .plusMinutes(1);
        //log.info("Rounded start time to [{}]", roundedStartDateTime);
        if (roundedStartDateTime.isBefore(LocalDateTime.now())) {
            //log.warn("❗️ Rounded start time [{}] is in the past", roundedStartDateTime);
            throw new RestApiException("Start date and time must be in the future");
        }
        if (endDateTime.isBefore(roundedStartDateTime)) {
            //log.warn("❗️ End time [{}] is before rounded start time [{}]", endDateTime, roundedStartDateTime);
            throw new RestApiException("End date and time must be after the start date and time");
        }
        List<CarResponseDto> availableCars = getAllAvailableCarsByDates(roundedStartDateTime, endDateTime);
        //log.info("✅ Total available cars by date: {}", availableCars.size());

        List<CarResponseDto> filteredCars = availableCars.stream()
                .peek(car -> log.debug("🔍 Checking car: id={}, brand={}, type={}, fuel={}, transmission={}, price={}",
                        car.id(), car.brand(), car.type(), car.fuelType(), car.transmissionType(), car.dayRentalPrice()))
                .filter(car ->
                        brand == null || brand.isEmpty() ||
                                brand.stream().anyMatch(item -> item.equalsIgnoreCase(car.brand())))
                .filter(car ->
                        type == null || type.isEmpty() ||
                                type.stream().anyMatch(item -> item.equalsIgnoreCase(car.type())))
                .filter(car ->
                        fuel == null || fuel.isEmpty() ||
                                fuel.stream().anyMatch(item -> item.equalsIgnoreCase(car.fuelType())))
                .filter(car ->
                        transmissionType == null || transmissionType.isEmpty() ||
                                transmissionType.stream().anyMatch(item -> item.equalsIgnoreCase(car.transmissionType())))
                .filter(car ->
                        (minPrice == null || car.dayRentalPrice().compareTo(minPrice) >= 0) &&
                                (maxPrice == null || car.dayRentalPrice().compareTo(maxPrice) <= 0))
                .sorted(Comparator.comparing(CarResponseDto::dayRentalPrice))
                .toList();

        //log.info("✅ Cars after full filtering: {}", filteredCars.size());
        return filteredCars;
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
        String fileName = "cars/" + id + "_" + System.currentTimeMillis() + "." + fileExtension;
        String imageUrl = carImageService.uploadToSpaces(fileName, file);
        car.setCarImage(imageUrl);
        carRepository.save(car);
        log.info("Image uploaded successfully for car ID: {}", id);
        return imageUrl;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("Invalid file format. File must have an extension.");
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}

