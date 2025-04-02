package de.aittr.car_rent.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CustomerUpdateRequestDto(

        @NotBlank(message = "{customer.firstName.notBlank}")
        @Schema(description = "Customer's first name", example = "John")
        String firstName,

        @NotBlank(message = "{customer.lastName.notBlank}")
        @Schema(description = "Customer's last name", example = "Doe")
        String lastName,

        @NotBlank(message = "{customer.email.notBlank}")
        @Schema(description = "Customer's email address", example = "john.doe@example.com")
        String email

) {}
