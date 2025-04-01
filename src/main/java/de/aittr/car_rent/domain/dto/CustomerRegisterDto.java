package de.aittr.car_rent.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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
        @Size(min = 8, message = "{customer.password.invalidSize}")
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",
                message = "{customer.password.invalidFormat}")
        @Schema(description = "Customer's password", example = "Password123!@#")
        String password
) {}
