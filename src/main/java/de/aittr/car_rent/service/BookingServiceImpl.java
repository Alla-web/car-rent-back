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
import org.springframework.security.core.context.SecurityContextHolder;
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
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) {

        if (bookingRequestDto.rentalStartDate().toLocalDate().isBefore(LocalDate.now())) {
            throw new RestApiException("Rental start date must be in the future.");
        }

        if (!bookingRequestDto.rentalEndDate().isAfter(bookingRequestDto.rentalStartDate())) {
            throw new RestApiException("Rental end date must be at least one day after the start date.");
        }

        Car car = carRepository.findById(bookingRequestDto.carId())
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //получили имейл текущего покупателя
        Customer currentCustomer = customerRepository.findByEmail(email)
                .orElseThrow(()-> new RestApiException("Customer not found"));

        long days = ChronoUnit.DAYS.between(bookingRequestDto.rentalStartDate(), bookingRequestDto.rentalEndDate());

        BigDecimal totalPrice = BigDecimal.valueOf(days + 1).multiply(car.getDayRentalPrice());

        if(car.getCarStatus() == CarStatus.AVAILABLE) {
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
        booking = bookingRepository.save(booking);
        return bookingMapper.mapEntityToDto(booking);
    }

    @Override
    public BookingResponseDto getBookingById(Long id) {
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
    public BookingResponseDto cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
            throw new RestApiException("Cannot cancel a completed booking");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED_BY_USER);
        booking.setUpdateBookingDate(LocalDateTime.now());

        bookingRepository.save(booking);
        return bookingMapper.mapEntityToDto(booking);
    }

    @Override
    @Transactional
    public BookingResponseDto extendBooking(Long id, LocalDateTime newEndDate) {
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
    public BookingResponseDto restoreBooking(Long id) {
        // TODO: Реализовать логику восстановления бронирования
        return null;
    }

}
