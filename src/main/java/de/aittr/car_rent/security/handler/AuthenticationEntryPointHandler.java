package de.aittr.car_rent.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author Andrej Reutow
 * created on 25.11.2023
 */
@Slf4j
public class AuthenticationEntryPointHandler extends AbstractHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
         log.warn("Authentication failed: {}", authException.getMessage());
        fillResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
    }
}
