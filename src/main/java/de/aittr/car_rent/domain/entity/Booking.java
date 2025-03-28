package de.aittr.car_rent.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private Long id;

    @Column(name = "rental_start_date", nullable = false)
    private LocalDateTime rentalStartDate;

    @Column(name = "rental_end_date")
    private LocalDateTime rentalEndDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status",  length = 20, nullable = false)
    private BookingStatus bookingStatus;

    @Column(name = "create_booking_date", nullable = false, updatable = false)
    private LocalDateTime createBookingDate;

    @Column(name = "update_booking_date")
    private LocalDateTime updateBookingDate;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @PrePersist
    @PreUpdate
    private void calculateTotalPriceAndSetDates() {
        if (rentalStartDate == null || rentalEndDate == null || car == null) {
            return;
        }


        long days = ChronoUnit.DAYS.between(rentalStartDate.toLocalDate(), rentalEndDate.toLocalDate());
        if (days <= 0) {
            throw new IllegalArgumentException("Rental end date must be at least one day after start date.");
        }

        if (car.getDayRentalPrice() == null) {
            throw new IllegalStateException("The price for renting a car has not been set.");
        }

        this.totalPrice = BigDecimal.valueOf(days).multiply(car.getDayRentalPrice());


        if (this.createBookingDate == null) {
            this.createBookingDate = LocalDateTime.now();
        }
        this.updateBookingDate = LocalDateTime.now();
    }
        }










