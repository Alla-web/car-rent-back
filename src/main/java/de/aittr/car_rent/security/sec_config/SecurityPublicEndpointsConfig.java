package de.aittr.car_rent.security.sec_config;

import org.springframework.util.AntPathMatcher;

import java.util.List;

public class SecurityPublicEndpointsConfig {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/cars",
            "/api/cars/id/**",
            "/brand/{brand}",
            "/model/{model}",
            "/year/{year}",
            "/type/{type}",
            "/fuel-type/{fuelType}",
            "/transmission-type/{transmissionType}",
            "/rental-price/{minDayRentalPrice}-{maxDayRentalPrice}",

            "/api/auth/login"
    );

    public static boolean isPublicEndpoint(String path) {
        return PUBLIC_ENDPOINTS.stream().anyMatch(p -> pathMatcher.match(p, path));
    }

    public static String[] getPublicEndpoints() {
        return PUBLIC_ENDPOINTS.toArray(new String[0]);
    }
}
