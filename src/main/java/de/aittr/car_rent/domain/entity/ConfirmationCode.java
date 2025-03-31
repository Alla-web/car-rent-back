package de.aittr.car_rent.domain.entity;

import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.management.ConstructorParameters;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "confirm_code")
@NoArgsConstructor
public class ConfirmationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "expired")
    private LocalDateTime expired;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public ConfirmationCode(Customer customer, LocalDateTime expired, String code) {
        this.customer = customer;
        this.expired = expired;
        this.code = code;
    }

}
