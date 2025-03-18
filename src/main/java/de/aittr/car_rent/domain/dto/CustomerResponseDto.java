package de.aittr.car_rent.domain.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class CustomerResponseDto {

    @Setter(AccessLevel.NONE)
    private Long id;

    private String firstName;

    private String lastName;

    private boolean is18;

    private String email;

}
