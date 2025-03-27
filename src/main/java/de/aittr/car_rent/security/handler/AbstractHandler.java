package de.aittr.car_rent.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.aittr.car_rent.security.dto.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * @author Andrej Reutow
 * created on 25.11.2023
 */
public abstract class AbstractHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void fillResponse(HttpServletResponse response, int statusCode, String message) {

        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String jsonResponse;
        try {
            jsonResponse = objectMapper.writeValueAsString(new AuthResponse(message));
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
