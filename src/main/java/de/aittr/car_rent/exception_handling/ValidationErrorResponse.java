package de.aittr.car_rent.exception_handling;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Response payload for validation errors")
public record ValidationErrorResponse(
        @Schema(description = "General error message", example = "Validation failed")
        String message,

        @Schema(description = "Map of field errors with details. The key is the field name, " +
                "and the value is an object containing the error message and rejected value.")
        Map<String, Object> errors
) {
}
