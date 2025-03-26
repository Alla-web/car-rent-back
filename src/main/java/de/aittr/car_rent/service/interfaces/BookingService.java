package de.aittr.car_rent.service.interfaces;

import de.aittr.car_rent.domain.dto.BookingRequestDto;
import de.aittr.car_rent.domain.dto.BookingResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto);
    BookingResponseDto getBookingById(Long id);
    List<BookingResponseDto> getAllBookings();
    List<BookingResponseDto> getBookingsByCarId(Long carId);
    BookingResponseDto extendBooking(Long id, LocalDateTime newEndDate);
    BookingResponseDto restoreBooking(Long id);
    BookingResponseDto cancelBooking(Long id);


}
