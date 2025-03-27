package de.aittr.car_rent.security.service;

import de.aittr.car_rent.domain.entity.Customer;
import de.aittr.car_rent.domain.entity.Role;
import de.aittr.car_rent.exception_handling.exceptions.RestApiException;
import de.aittr.car_rent.security.AuthInfo;
import de.aittr.car_rent.service.interfaces.CustomerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenService {

    private final SecretKey accessKey;
    private final SecretKey refreshKey;
    private final CustomerService customerService;

    public TokenService(
            @Value("${key.access}") String accessPhrase,
            @Value("${key.refresh}") String refreshPhrase,
            CustomerService customerService
    ) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(refreshPhrase));
        this.customerService = customerService;
    }

    //access - 24 hour, refresh - 1 week

    public String generateAccessToken(UserDetails user) {
        LocalDateTime currentDay = LocalDateTime.now();
        Instant expiration = currentDay.plusDays(1).atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(expiration);

        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(expirationDate)
                .signWith(accessKey)
                .compact();
    }

    public String generateRefreshToken(UserDetails user) {
        LocalDateTime currentDay = LocalDateTime.now();
        Instant expiration = currentDay.plusWeeks(1).atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(expiration);

        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(expirationDate)
                .signWith(refreshKey)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, accessKey);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, refreshKey);
    }

    private boolean validateToken(String token, SecretKey key) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //методы извлечения инфо из токенов
    public Claims getAccessClaims(String accessToken) {
        return getClaims(accessToken, accessKey);
    }

    public Claims getRafreshClaims(String refreshToken) {
        return getClaims(refreshToken, refreshKey);
    }

    private Claims getClaims(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //метод, который переделывает объект Claims в объект AuthInfo
    public AuthInfo mapClaimsToAuthInfo(Claims claims) {
        final String username = claims.getSubject();

        if (StringUtils.isNoneBlank(username)) {
            Role customerRole = customerService.findByEmail(username)
                    .map(Customer::getRole)
                    .orElseThrow(() -> new RestApiException("User not authenticated!", HttpStatus.FORBIDDEN));
            return new AuthInfo(username, customerRole);
        } else {
            throw new RestApiException("User not authenticated!", HttpStatus.FORBIDDEN);
        }
    }
}
