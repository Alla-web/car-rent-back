package de.aittr.car_rent.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CustomerResponseDto(

        @Schema(
                description = "Booking unique identifier",
                example = "123",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Long id,

        @Schema(description = "Customer first name", example = "John")
        String firstName,

        @Schema(description = "Customer last name", example = "Doe")
        String lastName,

        @Schema(description = "Customer's email address", example = "john.doe@example.com")
        String email,

        @Schema(
                description = "Customer role",
                example = "Customer",
                allowableValues = {"CUSTOMER", "ADMIN"})
        String role
) {
}
