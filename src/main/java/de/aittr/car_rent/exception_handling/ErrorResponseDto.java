package de.aittr.car_rent.exception_handling;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload for general errors")
public record ErrorResponseDto(
        @Schema(description = "Error message", example = "Email already exists")
        String message
) {
}
