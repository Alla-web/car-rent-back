package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.dto.BookingDto;
import de.aittr.car_rent.domain.entity.Booking;
import de.aittr.car_rent.domain.entity.BookingStatus;
import de.aittr.car_rent.domain.entity.Customer;
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

import java.time.LocalDateTime;
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
    public BookingDto createBooking(BookingDto bookingDto, Long customerId) {


        if (bookingDto.rentalStartDate().isBefore(LocalDateTime.now())) {
             throw new RestApiException("Rental start date must be in the future.");
        }

        if (bookingDto.rentalStartDate().isAfter(bookingDto.rentalEndDate())) {
             throw new RestApiException("Rental end date must be after the rental start date.");
        }


        carRepository.findById(bookingDto.carId())
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));


        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));


        Booking booking = bookingMapper.mapDtoToEntity(bookingDto);
        booking.setCustomer(customer);
        booking = bookingRepository.save(booking);

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
        return bookingRepository.findAllByCarId(carId).stream()
                .map(bookingMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }


    public BookingDto cancelBooking(Long id) {
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
