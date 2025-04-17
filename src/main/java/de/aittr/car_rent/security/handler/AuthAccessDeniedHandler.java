package de.aittr.car_rent.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.text.MessageFormat;

/**
 * @author Andrej Reutow
 * created on 25.11.2023
 */
public class AuthAccessDeniedHandler extends AbstractHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String message = MessageFormat.format("Access denied for user with email <{0}> and role {1}",
                authentication.getName(), authentication.getAuthorities());
        fillResponse(response, HttpServletResponse.SC_FORBIDDEN, message);
    }
}
