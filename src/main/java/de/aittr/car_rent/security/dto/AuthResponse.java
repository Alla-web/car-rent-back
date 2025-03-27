package de.aittr.car_rent.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Структура ответа в случае ошибки аутентификации/авторизации")
public record AuthResponse(
        @Schema(description = "Сообщение об ошибке", example = "User not authenticated")
        String message) {
}
