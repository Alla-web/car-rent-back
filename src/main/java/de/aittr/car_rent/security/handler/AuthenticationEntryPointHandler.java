package de.aittr.car_rent.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author Andrej Reutow
 * created on 25.11.2023
 */
public class AuthenticationEntryPointHandler extends AbstractHandler implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        fillResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
    }
}
