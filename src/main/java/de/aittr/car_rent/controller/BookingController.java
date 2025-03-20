package de.aittr.car_rent.controller;

import de.aittr.car_rent.domain.dto.BookingDto;
import de.aittr.car_rent.service.interfaces.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Booking controller", description = "Controller for various operations with Bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a new booking", description = "Creates a new booking for a car")
    public BookingDto createBooking(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Booking details")
            BookingDto bookingDto) {
        return bookingService.createBooking(bookingDto, bookingDto.customerId());
    }

    @GetMapping("/all")
    @Operation(summary = "Get all bookings", description = "Returns all bookings from the database")
    public List<BookingDto> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a booking by ID", description = "Returns a booking by its unique identifier")
    public BookingDto getBookingById(
            @PathVariable
            @Parameter(description = "Booking unique identifier") Long id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/by-car/{carId}")
    @Operation(summary = "Get bookings by car ID", description = "Returns bookings associated with a specific car")
    public List<BookingDto> getBookingsByCarId(@PathVariable Long carId) {
        return bookingService.getBookingsByCarId(carId);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel a booking", description = "Cancels a booking by its ID")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    @PutMapping("/{id}/extend")
    @Operation(summary = "Extend a booking", description = "Extends the rental period of an existing booking")
    public BookingDto extendBooking(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newEndDate) {
        return bookingService.extendBooking(id, newEndDate);
    }

    @PutMapping("/{id}/restore")
    @Operation(summary = "Restore a cancelled booking", description = "Restores a cancelled booking by its ID")
    public BookingDto restoreBooking(@PathVariable Long id) {
        return bookingService.restoreBooking(id);
    }

}
