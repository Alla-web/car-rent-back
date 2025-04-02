package de.aittr.car_rent.service.interfaces;

import de.aittr.car_rent.domain.dto.BookingRequestDto;
import de.aittr.car_rent.domain.dto.BookingResponseDto;
import de.aittr.car_rent.domain.entity.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto, String email);

    BookingResponseDto getBookingByBookingId(Long id);

    List<BookingResponseDto> getAllBookings();

    List<BookingResponseDto> getBookingsByCarId(Long carId);

    List<BookingResponseDto> getBookingsByRentalDaysOrByBookingStatus(LocalDate rentalStartDate, LocalDate rentalEndDate, BookingStatus bookingStatus);

    BookingResponseDto activateBooking(Long bookingId, String email);

    BookingResponseDto extendBooking(Long id, String email, LocalDateTime newEndDate);

    BookingResponseDto cancelBooking(Long id, String email);

    BookingResponseDto closeBooking(Long id, String email);

    BigDecimal calculateBookingTotalPrice(BigDecimal rentalDayPrice, LocalDateTime fromDate, LocalDateTime toDate);

}
