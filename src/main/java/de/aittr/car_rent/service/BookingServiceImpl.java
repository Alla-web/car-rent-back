package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.dto.BookingRequestDto;
import de.aittr.car_rent.domain.dto.BookingResponseDto;
import de.aittr.car_rent.domain.entity.*;
import de.aittr.car_rent.exception_handling.exceptions.BookingNotFoundException;
import de.aittr.car_rent.exception_handling.exceptions.RestApiException;
import de.aittr.car_rent.repository.BookingRepository;
import de.aittr.car_rent.repository.CarRepository;
import de.aittr.car_rent.repository.CustomerRepository;
import de.aittr.car_rent.service.interfaces.BookingService;
import de.aittr.car_rent.service.interfaces.CarService;
import de.aittr.car_rent.service.mapping.BookingMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final BookingMapper bookingMapper;
    private final CarService carService;

    @Override
    @Transactional
    public BookingResponseDto createBooking(@Valid BookingRequestDto bookingRequestDto, String email) {


        log.info("Booking request received: {}", bookingRequestDto);

        if (bookingRequestDto == null) {
            throw new RestApiException("Booking request cannot be null.");
        }

        if (bookingRequestDto.carId() == null) {
            throw new RestApiException("Car ID cannot be null.");
        }

        if (bookingRequestDto.rentalStartDate() == null || bookingRequestDto.rentalEndDate() == null) {
            throw new RestApiException("Rental start and end dates cannot be null.");
        }

        if (email == null || email.isEmpty())  {
            throw new RestApiException("Email cannot be null or empty.");
        }

        if (bookingRequestDto.rentalStartDate().toLocalDate().isBefore(LocalDate.now())) {
            throw new RestApiException("Rental start date must be today or in the future.");
        }

        if (!bookingRequestDto.rentalEndDate().isAfter(bookingRequestDto.rentalStartDate())) {
            throw new RestApiException("Rental end date must be at least one full day after the start date.");
        }

        Car car = carRepository.findById(bookingRequestDto.carId())
                .orElseThrow(() -> new RestApiException("Car not found"));
        log.info("Car with ID {} found. Proceeding to create booking.", bookingRequestDto.carId());

        Customer currentCustomer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException("Customer with this email " + email + " not found"));
        log.info("Customer with email '{}' found. Proceeding to create booking.", email);

        Duration duration = Duration.between(bookingRequestDto.rentalStartDate(), bookingRequestDto.rentalEndDate());
        BigDecimal totalPrice = calculateTotalPrice(car.getDayRentalPrice(), duration);

        boolean isAvailableCar = carService.checkIfCarAvailableByDates(car.getId(), bookingRequestDto.rentalStartDate(), bookingRequestDto.rentalEndDate());

        if (!isAvailableCar) {
           throw new RestApiException("Car with id " + car.getId() + " is not available during the period from " + bookingRequestDto.rentalStartDate() + " to " + bookingRequestDto.rentalEndDate());
        }

        Booking booking = new Booking();
        booking.setCreateBookingDate(LocalDateTime.now());
        booking.setUpdateBookingDate(LocalDateTime.now());
        booking.setRentalStartDate(bookingRequestDto.rentalStartDate());
        booking.setRentalEndDate(bookingRequestDto.rentalEndDate());
        booking.setCustomer(currentCustomer);
        booking.setCar(car);
        booking.setTotalPrice(totalPrice);
        booking.setBookingStatus(BookingStatus.PENDING);
        log.info("Saving booking for customer {} with car ID {} and total price {}", email, bookingRequestDto.carId(), totalPrice);
        booking = bookingRepository.save(booking);
        log.info("Booking successfully created for customer {} with car ID {}.", email, bookingRequestDto.carId());

        return bookingMapper.mapEntityToDto(booking);
    }

    private BigDecimal calculateTotalPrice(BigDecimal dailyPrice, Duration duration) {
        if (dailyPrice == null || dailyPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RestApiException("Daily price must be greater than zero.");
        }

        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        BigDecimal hourlyPrice = dailyPrice.divide(BigDecimal.valueOf(24), RoundingMode.HALF_UP);
        BigDecimal minutelyPrice = hourlyPrice.divide(BigDecimal.valueOf(60), RoundingMode.HALF_UP);
        BigDecimal secondlyPrice = minutelyPrice.divide(BigDecimal.valueOf(60), RoundingMode.HALF_UP);

        return dailyPrice.multiply(BigDecimal.valueOf(days))
                .add(hourlyPrice.multiply(BigDecimal.valueOf(hours)))
                .add(minutelyPrice.multiply(BigDecimal.valueOf(minutes)))
                .add(secondlyPrice.multiply(BigDecimal.valueOf(seconds)));
    }


    @Override
    public BookingResponseDto getBookingByBookingId(Long id) {

        if (id == null) {
            throw new RestApiException("Booking ID cannot be null.");
        }

        return bookingRepository.findById(id)
                .map(bookingMapper::mapEntityToDto)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    @Override
    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::mapEntityToDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<BookingResponseDto> getBookingsByCarId(Long carId) {

        if (carId == null) {
            throw new RestApiException("Car ID cannot be null.");
        }

        log.info("Request to receive reservations for a vehicle with ID: {}", carId);

        List<BookingResponseDto> bookings = bookingRepository.findAllByCarId(carId)
                .stream()
                .map(bookingMapper::mapEntityToDto)
                .collect(Collectors.toList());

        log.info("Found {} bookings for car {}", bookings.size(), carId);
        return bookings;

    }

    //TODO как правильно работает эта аннотация  и нужна ли она здесь?
    //@Transactional(readOnly = true)
    @Override
    public List<BookingResponseDto> getBookingsByRentalDaysOrByBookingStatus(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rentalStartDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rentalEndDate,
            BookingStatus bookingStatus) {

        log.info("Searching bookings with parameters - Start Date: {}, End Date: {}, Status: {}",
                rentalStartDate, rentalEndDate, bookingStatus);

        if (rentalStartDate == null && rentalEndDate == null && bookingStatus == null) {
            throw new RestApiException("Enter rental start date, rental end date, or booking status");

        }
            if (rentalStartDate != null && rentalEndDate != null) {
                throw new RestApiException("Enter only one date - rental start date or rental end date.");
            }

        return bookingRepository.findAll()
                .stream()
                .filter(booking -> {
                    boolean matchesStart = (rentalStartDate != null
                            && booking.getRentalStartDate().toLocalDate().isEqual(rentalStartDate));
                    boolean matchesEnd = (rentalEndDate != null
                            && booking.getRentalEndDate().toLocalDate().isEqual(rentalEndDate));
                    return rentalStartDate == null && rentalEndDate == null || matchesStart || matchesEnd;
                })
                .filter(booking -> bookingStatus == null || booking.getBookingStatus() == bookingStatus)
                .sorted(Comparator.comparing(Booking::getBookingStatus))
                .map(bookingMapper::mapEntityToDto)
                .toList();
    }

    @Transactional
    @Override
    public BookingResponseDto extendBooking( Long id,  String email,   LocalDateTime newEndDate) {

        log.info("Attempting to extend booking with ID: {} by user: {}", id, email);

        if (newEndDate == null) {
            throw new RestApiException("New rental end date must not be null.");
        }

        Customer currentCustomer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));

        Booking currentBbooking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        Car rentedCar = carRepository.findById(currentBbooking.getCar().getId())
                .orElseThrow(() -> new RuntimeException("Car not found for booking ID: " + id));

        if (currentBbooking.getBookingStatus() != BookingStatus.ACTIVE) {
            throw new RestApiException("Cannot extend a booking that is not active.");
        }

        if (newEndDate.isBefore(currentBbooking.getRentalEndDate())) {
            throw new RestApiException("New rental end date must be after the current rental end date.");
        }

        if (currentCustomer.getRole().getName().equalsIgnoreCase("ROLE_ADMIN")) {
            rentedCar.setCarStatus(CarStatus.RENTED);
            carRepository.save(rentedCar);
            currentBbooking.setRentalEndDate(newEndDate);
            currentBbooking.setUpdateBookingDate(LocalDateTime.now());
            bookingRepository.save(currentBbooking);
        } else {
            if (!currentBbooking.getCustomer().equals(currentCustomer)) {
                log.warn("Customer with email {} is trying to extend currentBooking ID: {} which belongs to customer ID: {}",
                        email, id, currentBbooking.getCustomer().getId());
                throw new RestApiException("You can only extend your own bookings");
            }

            boolean isCarAvailable = carService.checkIfCarAvailableByDates(currentBbooking.getCar().getId(), currentBbooking.getRentalEndDate(), newEndDate);

            if (!isCarAvailable) {
                throw new RestApiException("Car with id " + currentBbooking.getCar().getId() + " is already booked during the period from " + currentBbooking.getRentalEndDate() + " to" + newEndDate);
            }
            rentedCar.setCarStatus(CarStatus.RENTED);
            carRepository.save(rentedCar);

            currentBbooking.setRentalEndDate(newEndDate);
            currentBbooking.setUpdateBookingDate(LocalDateTime.now());
            bookingRepository.save(currentBbooking);
        }

        log.info("Successfully extended booking ID: {} to new end date: {}", id, newEndDate);
        return bookingMapper.mapEntityToDto(currentBbooking);
    }


    private boolean checkIfCarAvailable(Long currentBookingId, Long carId, LocalDateTime from, LocalDateTime to) {
        List<Booking> conflictBookings = bookingRepository.findAllByCarId(carId)
                .stream()
                .filter(b -> !b.getId().equals(currentBookingId))
                .filter(b -> b.getBookingStatus() == BookingStatus.ACTIVE)
                .filter(b -> b.getRentalStartDate().isBefore(to) && b.getRentalEndDate().isAfter(from))
                .toList();
        return conflictBookings.isEmpty();
    }

    @Transactional
    public BookingResponseDto cancelBooking(Long id, String email) {
        log.info("Attempting to cancel booking with ID: {}", id);

        if (id == null) {
            throw new RestApiException("Booking ID cannot be null");
        }

        if (email == null || email.isEmpty()) {
            log.warn("Email is null or empty");
            throw new RestApiException("Email cannot be null or empty");
        }

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->  new RestApiException("Booking with ID " + id + " not found"));

        if (booking.getBookingStatus() == BookingStatus.CLOSED_BY_ADMIN) {
            log.warn("Cannot cancel a completed booking with ID: {}", id);
            throw new RestApiException("Cannot cancel a completed booking");
        }

        if (booking.getBookingStatus() == BookingStatus.CANCELLED_BY_ADMIN ||
                booking.getBookingStatus() == BookingStatus.CANCELLED_BY_USER) {
            log.warn("Booking with ID: {} is already cancelled", id);
            throw new RestApiException("Booking is already cancelled");
        }

        if (booking.getRentalEndDate().isBefore(LocalDateTime.now())) {
            throw new RestApiException("Cannot cancel a booking that has already ended.");
        }

        Customer currentCustomer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException("Customer with email " + email + " not found"));

        if (currentCustomer.getRole().getName().equals("ROLE_ADMIN")) {
            booking.setBookingStatus(BookingStatus.CANCELLED_BY_ADMIN);
        } else {
            if (!booking.getCustomer().getId().equals(currentCustomer.getId())) {
                log.warn("Customer with email {} is trying to cancel booking ID: {} which belongs to customer ID: {}",
                        email, id, booking.getCustomer().getId());
                throw new RestApiException("You can only cancel your own bookings");
            }
            booking.setBookingStatus(BookingStatus.CANCELLED_BY_USER);
        }

        log.info("Changing car status booked car to AVAILABLE");
        Car bookedCar = carRepository.findById(booking.getCar().getId())
                .orElseThrow(() -> new RestApiException("Car from the booking not found"));
        bookedCar.setCarStatus(CarStatus.AVAILABLE);
        carRepository.save(bookedCar);

        booking.setUpdateBookingDate(LocalDateTime.now());
        bookingRepository.save(booking);

        log.info("Successfully cancelled booking with ID: {}", id);
        return bookingMapper.mapEntityToDto(booking);
    }

    @Transactional
    @Override
    public void closeBooking(Long id, String email) {
        log.info("Attempting to close booking with ID: {}", id);

        if (email == null || email.trim().isEmpty()) {
            throw new RestApiException("Email cannot be null or empty");
        }

        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RestApiException("Booking with id: " + id + " not found"));
        Customer currentCustomer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException("Customer not found"));
        Car bookedCar = carRepository.findById(existingBooking.getCar().getId())
                .orElseThrow(() -> new RestApiException("Car from the booking not found"));

        if (!currentCustomer.getRole().getName().equalsIgnoreCase("ROLE_ADMIN")) {
            throw new RestApiException("Only an administrator can close bookings");
        } if (existingBooking.getBookingStatus() == BookingStatus.CLOSED_BY_ADMIN) {
            throw new RestApiException("Booking is already closed");
        }

        if (existingBooking.getBookingStatus() == BookingStatus.CANCELLED_BY_ADMIN ||
                existingBooking.getBookingStatus() == BookingStatus.CANCELLED_BY_USER) {
            throw new RestApiException("Cannot close a cancelled booking");
        }

        if (existingBooking.getBookingStatus() == BookingStatus.ACTIVE) {
            throw new RestApiException("Cannot close an active booking. Mark it as completed first.");
        }

            existingBooking.setBookingStatus(BookingStatus.CLOSED_BY_ADMIN);
            existingBooking.setUpdateBookingDate(LocalDateTime.now());
            bookingRepository.save(existingBooking);

            bookedCar.setCarStatus(CarStatus.UNDER_INSPECTION);
            carRepository.save(bookedCar);

            log.info("Booking with ID {} closed by admin. Car set to UNDER_INSPECTION.", id);
        }

    }

