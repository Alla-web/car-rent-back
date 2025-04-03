package de.aittr.car_rent.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Car Rent API", version = "1.0", description = "API для аренды автомобилей"),
        servers = {
                @Server(url = "http://localhost:8080/api", description = "Локальный сервер"),
                @Server(url = "https://car-rental-cymg8.ondigitalocean.app/api", description = "Деплой сервер"),
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
