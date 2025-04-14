package de.aittr.car_rent.controller;

import de.aittr.car_rent.domain.dto.BookingRequestDto;
import de.aittr.car_rent.domain.dto.BookingResponseDto;
import de.aittr.car_rent.domain.entity.BookingStatus;
import de.aittr.car_rent.service.interfaces.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public BookingResponseDto createBooking(
            @AuthenticationPrincipal
            @Parameter(hidden = true)
            String userEmail,

            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Booking details")
            BookingRequestDto bookingDto) {
        return bookingService.createBooking(bookingDto, userEmail);
    }

    @GetMapping
    @Operation(summary = "Get all bookings existing in the database", description = "Returns all bookings from the database")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public List<BookingResponseDto> getAllBookings() {
        return bookingService.getAllBookings();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a booking by ID", description = "Returns a booking by its unique identifier")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public BookingResponseDto getBookingByBookingId(
            @PathVariable
            @Parameter(description = "Booking unique identifier", example = "7") Long id) {
        return bookingService.getBookingByBookingId(id);
    }

    @GetMapping("/filter/by-car")
    @Operation(summary = "Get bookings by car ID", description = "Returns bookings associated with a specific car")
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN'})")
    @SecurityRequirement(name = "bearerAuth")
    public List<BookingResponseDto> getBookingsByCarId(
            @RequestParam("car-id")
            @Parameter(description = "Car unique identifier", example = "12")
            Long carId) {
        return bookingService.getBookingsByCarId(carId);
    }

    @GetMapping("/filter/by-rental-dates-or-by-bookings-status")
    @Operation(
            summary = "Get bookings by rental start or end date and by booking status or only by by booking status ",
            description = "Filters bookings by rental start or end date and by booking status or only by booking status ")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public List<BookingResponseDto> getBookingsByRentalDaysOrByBookingStatus(
            @RequestParam(value = "start-date", required = false)
            @Parameter(
                    description = "Rental start date",
                    example = "2025-03-28")
            LocalDate rentalStartDate,

            @RequestParam(value = "end-date", required = false)
            @Parameter(
                    description = "Rental end date",
                    example = "2025-03-28")
            LocalDate rentalEndDate,

            @RequestParam(value = "car-status", required = false)
            @Parameter(description = "Car status in the data base", example = "ACTIVE")
            BookingStatus bookingStatus) {
        return bookingService.getBookingsByRentalDaysOrByBookingStatus(rentalStartDate, rentalEndDate, bookingStatus);
    }

    @PutMapping("activate/{id}")
    @Operation(
            summary = "Activates booking at the moment of car delivery to customer",
            description = "Changes booking status to ACTIVE and car status to RENTED")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public BookingResponseDto activateBooking(
            @PathVariable("id")
            @Parameter(description = "Booking unique identifier", example = "7")
            Long bookingId,

            @AuthenticationPrincipal
            @Parameter(hidden = true)
            String email) {
        return bookingService.activateBooking(bookingId, email);
    }

    @PutMapping("/extend/{id}")
    @Operation(summary = "Extend a booking", description = "Extends the rental period of an existing booking")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public BookingResponseDto extendBooking(
            @PathVariable
            @Parameter(description = "Booking unique identifier", example = "7")
            Long id,

            @AuthenticationPrincipal
            @Parameter(hidden = true)
            String email,

            @RequestParam
            @Parameter(
                    description = "New rental end date",
                    example = "2025-04-01T00:00")
            LocalDateTime newEndDate) {
        return bookingService.extendBooking(id, email, newEndDate);
    }

    @PutMapping("/cancel/{id}")
    @Operation(summary = "Cancel a booking", description = "Cancels a booking by its ID")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BookingResponseDto> cancelBooking(
            @PathVariable
            @Parameter(description = "Booking unique identifier", example = "17")
            Long id,

            @AuthenticationPrincipal
            @Parameter(hidden = true)
            String email) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, email));
    }

    @PutMapping("close/{id}")
    @Operation(
            summary = "Closes active booking",
            description = "Changes bookings status from ACTIVE to CLOSED")
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN'})")
    @SecurityRequirement(name = "bearerAuth")
    public BookingResponseDto closeBooking(
            @PathVariable
            @Parameter(description = "Booking unique identifier", example = "17")
            Long id,

            @AuthenticationPrincipal
            String email) {
        return bookingService.closeBooking(id, email);
    }
}
