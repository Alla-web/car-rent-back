package de.aittr.car_rent.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "rental_start_date", nullable = false)
    private LocalDateTime rentalStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "rental_end_date")
    private LocalDateTime rentalEndDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", length = 20, nullable = false)
    private BookingStatus bookingStatus;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "create_booking_date", nullable = false, updatable = false)
    private LocalDateTime createBookingDate;

    @Column(name = "update_booking_date")
    private LocalDateTime updateBookingDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @PrePersist
    @PreUpdate
    private void setDates() {
        if (this.createBookingDate == null) {
            this.createBookingDate = LocalDateTime.now();
        }
        this.updateBookingDate = LocalDateTime.now().withSecond(0).withNano(0).plusMinutes(1);
    }
}










