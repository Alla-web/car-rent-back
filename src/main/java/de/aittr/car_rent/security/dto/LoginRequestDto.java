package de.aittr.car_rent.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO representing a login request.")
public record LoginRequestDto(
        @NotBlank(message = "{customer.email.notBlank}")
        @Schema(description = "User's email address", examples = {"customer_1@car-rent.de", "customer_2@cr.de", "customer_3@cr.de", "admin_1@car-rent.de"})
        String email,

        @NotBlank(message = "{customer.password.notBlank}")
        @Schema(description = "User's password", example = "User-pass#007")
        String password
) {
}
