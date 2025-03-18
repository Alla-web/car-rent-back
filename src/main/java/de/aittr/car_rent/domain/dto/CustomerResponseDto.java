package de.aittr.car_rent.domain.dto;

public record CustomerResponseDto(
        Long id,
        String firstName,
        String lastName,
        boolean is18,
        String email
) {
}
