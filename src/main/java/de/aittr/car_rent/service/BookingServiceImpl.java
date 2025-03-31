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
import de.aittr.car_rent.service.mapping.BookingMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto, String email) {

        if (bookingRequestDto.rentalStartDate().toLocalDate().isBefore(LocalDate.now())) {
            throw new RestApiException("Rental start date must be today or in the future.");
        }

        if (!bookingRequestDto.rentalEndDate().isAfter(bookingRequestDto.rentalStartDate())) {
            throw new RestApiException("Rental end date must be at least one day after the start date.");
        }

        Car car = carRepository.findById(bookingRequestDto.carId())
                .orElseThrow(() -> new RestApiException("Car not found"));

        log.info("Car with ID {} found. Proceeding to create booking.", bookingRequestDto.carId());

        Customer currentCustomer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException("Customer not found"));

        long days = ChronoUnit.DAYS.between(bookingRequestDto.rentalStartDate(), bookingRequestDto.rentalEndDate());

        BigDecimal totalPrice = BigDecimal.valueOf(days + 1).multiply(car.getDayRentalPrice());

        if (car.getCarStatus() == CarStatus.AVAILABLE) {
            car.setCarStatus(CarStatus.RENTED);
            carRepository.save(car);
        } else {
            throw new RestApiException("Car is not available");
        }

        Booking booking = bookingMapper.mapDtoToEntity(bookingRequestDto);
        booking.setCreateBookingDate(LocalDateTime.now());
        booking.setCustomer(currentCustomer);
        booking.setCar(car);
        booking.setTotalPrice(totalPrice);
        booking.setBookingStatus(BookingStatus.ACTIVE);
        booking.setUpdateBookingDate(LocalDateTime.now());
        log.info("Saving booking for customer {} with car ID {} and total price {}", email, bookingRequestDto.carId(), totalPrice);
        booking = bookingRepository.save(booking);
        log.info("Booking successfully created for customer {} with car ID {}.", email, bookingRequestDto.carId());
        return bookingMapper.mapEntityToDto(booking);
    }

    @Override
    public BookingResponseDto getBookingByBookingId(Long id) {
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
    public List<BookingResponseDto> getBookingsByRentalDaysOrByBookingStatus(LocalDate rentalStartDate, LocalDate rentalEndDate, BookingStatus bookingStatus) {

        if (rentalStartDate != null && rentalEndDate != null) {
            throw new RestApiException("Enter only one date - rental start date or rental end date");
        } else if (rentalStartDate == null && rentalEndDate == null && bookingStatus == null) {
            throw new RestApiException("Enter rental start date or end day or booking status");
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
    public BookingResponseDto extendBooking(Long id, String email, LocalDateTime newEndDate) {
        Customer currentCustomer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Booking currentBbooking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        Car rentedCar = carRepository.findById(currentBbooking.getCar().getId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

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
            if(!currentBbooking.getCustomer().getId().equals(currentCustomer.getId())) {
                log.warn("Customer with email {} is trying to extend currentBbooking ID: {} which belongs to customer ID: {}",
                        email, id, currentBbooking.getCustomer().getId());
                throw new RestApiException("You can only extend your own bookings");
            }
            if (checkIfCarAvailable(currentBbooking.getId(), currentBbooking.getCar().getId(), currentBbooking.getRentalEndDate(), newEndDate)) {
                throw new RestApiException("Car is already booked during the extension period from " + currentBbooking.getRentalEndDate() + " to" + newEndDate);
            }
            rentedCar.setCarStatus(CarStatus.RENTED);
            carRepository.save(rentedCar);

            currentBbooking.setRentalEndDate(newEndDate);
            currentBbooking.setUpdateBookingDate(LocalDateTime.now());
            bookingRepository.save(currentBbooking);
        }
        return bookingMapper.mapEntityToDto(currentBbooking);
    }

    private boolean checkIfCarAvailable(Long currentBookingId, Long carId, LocalDateTime from, LocalDateTime to) {
        List<Booking> conflictBookings = bookingRepository.findAllByCarId(carId)
                .stream()
                .filter(b -> !b.getId().equals(currentBookingId))
                .filter(b -> b.getBookingStatus() == BookingStatus.ACTIVE)
                .filter(b -> b.getRentalStartDate().isBefore(to) && b.getRentalEndDate().isAfter(from.plusNanos(1)))
                .toList();
        return conflictBookings.isEmpty();
    }

    @Transactional
    public BookingResponseDto cancelBooking(Long id, String email) {
        log.info("Attempting to cancel booking with ID: {}", id);

        log.info("Finding the booking with ID: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
            log.warn("Cannot cancel a completed booking with ID: {}", id);
            throw new RestApiException("Cannot cancel a completed booking");
        }

        if (booking.getBookingStatus() == BookingStatus.CANCELLED_BY_ADMIN ||
                booking.getBookingStatus() == BookingStatus.CANCELLED_BY_USER) {
            log.warn("Booking with ID: {} is already cancelled", id);
            throw new RestApiException("Booking is already cancelled");
        }

        log.info("Finding the current customer with email: {}", email);
        Customer currentCustomer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Current customer not found"));

        log.info("Cancelling booking with ID: {}", id);

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
        return bookingMapper.mapEntityToDto(booking);
    }

    @Transactional
    @Override
    public void closeBooking(Long id, String email) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RestApiException("Booking with id: " + id + " not found"));
        Customer currentCustomer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException("Customer not found"));
        Car bookedCar = carRepository.findById(existingBooking.getCar().getId())
                .orElseThrow(() -> new RestApiException("Car from the booking not found"));

        if (!currentCustomer.getRole().getName().equalsIgnoreCase("ROLE_ADMIN")) {
            throw new RestApiException("Close booking can only administrator");
        } else {
            existingBooking.setBookingStatus(BookingStatus.CLOSED_BY_ADMIN);
            existingBooking.setUpdateBookingDate(LocalDateTime.now());
            bookingRepository.save(existingBooking);
            bookedCar.setCarStatus(CarStatus.UNDER_INSPECTION);
            carRepository.save(bookedCar);
            log.info("Booking with ID {} closed by admin. Car set to UNDER_INSPECTION.", id);
        }

    }
}
