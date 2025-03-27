package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.dto.BookingDto;
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
    public BookingDto createBooking(BookingDto bookingDto, Long customerId) {
        log.info("Creating booking for customer: {}", customerId);

        if (bookingDto.rentalStartDate().toLocalDate().isBefore(LocalDate.now())) {
            throw new RestApiException("Rental start date must be today or in the future.");
        }

        if (!bookingDto.rentalEndDate().isAfter(bookingDto.rentalStartDate())) {
            throw new RestApiException("Rental end date must be after the start date.");
        }

        Car car = carRepository.findById(bookingDto.carId())
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));

        log.info("Car with ID {} found. Proceeding to create booking.", bookingDto.carId());

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        log.info("Customer with ID {} found. Proceeding to create booking.", customerId);

        long days = ChronoUnit.DAYS.between(
                bookingDto.rentalStartDate().toLocalDate(),
                bookingDto.rentalEndDate().toLocalDate()
        );

        BigDecimal totalPrice = BigDecimal.valueOf(days).multiply(car.getDayRentalPrice());

        car.setCarStatus(CarStatus.RENTED);
        carRepository.save(car);

        Booking booking = bookingMapper.mapDtoToEntity(bookingDto);
        booking.setCustomer(customer);
        booking.setCar(car);
        booking.setTotalPrice(totalPrice);
        booking.setBookingStatus(BookingStatus.ACTIVE);

        log.info("Saving booking for customer {} with car ID {} and total price {}", customerId, bookingDto.carId(), totalPrice);

        booking = bookingRepository.save(booking);

        log.info("Booking successfully created for customer {} with car ID {}.", customerId, bookingDto.carId());

        return bookingMapper.mapEntityToDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(bookingMapper::mapEntityToDto)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByCarId(Long carId) {
        log.info("Request to receive reservations for a vehicle with ID: {}", carId);

        List<BookingDto> bookings = bookingRepository.findAllByCarId(carId)
                .stream()
                .map(bookingMapper::mapEntityToDto)
                .collect(Collectors.toList());

        log.info("Found {} bookings for car {}", bookings.size(), carId);
        return bookings;

    }
    public BookingDto cancelBooking(Long id) {
        log.info("Attempting to cancel booking with ID: {}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
            log.warn("Cannot cancel a completed booking with ID: {}", id);
            throw new RestApiException("Cannot cancel a completed booking");
        }

        log.info("Cancelling booking with ID: {}", id);
        booking.setBookingStatus(BookingStatus.CANCELLED_BY_USER);
        booking.setUpdateBookingDate(LocalDateTime.now());

        bookingRepository.save(booking);
        return bookingMapper.mapEntityToDto(booking);
    }

    @Override
    @Transactional
    public BookingDto extendBooking(Long id, LocalDateTime newEndDate) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        if (newEndDate.isBefore(booking.getRentalEndDate())) {
             throw new RestApiException("New rental end date must be after the current rental end date.");
        }

        booking.setRentalEndDate(newEndDate);
        booking.setUpdateBookingDate(LocalDateTime.now());

        return bookingMapper.mapEntityToDto(booking);
    }

    @Override
    @Transactional
    public BookingDto restoreBooking(Long id) {
        // TODO: Реализовать логику восстановления бронирования
        return null;
    }

}
