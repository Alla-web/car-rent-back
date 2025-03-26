package de.aittr.car_rent.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO representing a customer registration request.")
public record CustomerRegisterDto(
        @NotBlank(message = "{customer.firstName.notBlank}")
        @Schema(description = "Customer's first name", example = "John")
        String firstName,

        @NotBlank(message = "{customer.lastName.notBlank}")
        @Schema(description = "Customer's last name", example = "Doe")
        String lastName,

        @NotBlank(message = "{customer.email.notBlank}")
        @Schema(description = "Customer's email address", example = "john.doe@example.com")
        String email,

        @NotBlank(message = "{customer.password.notBlank}")
        @Schema(description = "Customer's password", example = "mypassword123")
        String password
) {
}
