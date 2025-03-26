package de.aittr.car_rent.domain.dto;

import de.aittr.car_rent.domain.entity.Role;

public record CustomerResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String role
) {
}
