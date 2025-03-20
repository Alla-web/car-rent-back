package de.aittr.car_rent.service.interfaces;

import de.aittr.car_rent.domain.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    BookingDto createBooking(BookingDto bookingDTO, Long customerId);
    BookingDto getBookingById(Long id);
    List<BookingDto> getAllBookings();
    List<BookingDto> getBookingsByCarId(Long carId);
    BookingDto extendBooking(Long id, LocalDateTime newEndDate);
    BookingDto restoreBooking(Long id);
    BookingDto cancelBooking(Long id);


}
