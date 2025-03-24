package de.aittr.car_rent.security.sec_service;

import de.aittr.car_rent.exception_handling.exceptions.RestApiException;
import de.aittr.car_rent.security.sec_dto.LoginRequestDto;
import de.aittr.car_rent.security.sec_dto.TokenResponseDto;
import de.aittr.car_rent.service.interfaces.CustomerService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final CustomerService customerService;
    private final TokenService tokenService;
    private final Map<String, String> refreshStorage;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(
            CustomerService customerService,
            TokenService tokenService,
            BCryptPasswordEncoder encoder) {
        this.customerService = customerService;
        this.tokenService = tokenService;
        this.passwordEncoder = encoder;
        this.refreshStorage = new HashMap<>();
    }

    //метод первичной проверки пароля и логина и отдачи ему access и refresh токенов
    public TokenResponseDto login(LoginRequestDto inboundCustomer) {
        String username = inboundCustomer.email();
        UserDetails foundUser = customerService.findByEmailOrThrow(username);

        if (passwordEncoder.matches(inboundCustomer.password(), foundUser.getPassword())) {
            String accessToken = tokenService.generateAccessToken(foundUser);
            String refreshToken = tokenService.generateRefreshToken(foundUser);
            refreshStorage.put(username, refreshToken);
            return new TokenResponseDto(accessToken, refreshToken);
        } else {
            throw new RestApiException("Password is incorrect", HttpStatus.FORBIDDEN);
        }
    }

    //метод выдачи нового access-токена (когда он истек) на основании refresh токена
    public TokenResponseDto getNewAccessToken(String inboundRefreshToken){

        Claims refreshClaims = tokenService.getRafreshClaims(inboundRefreshToken);
        String username = refreshClaims.getSubject();
        String foundRefreshToken = refreshStorage.get(username);
        if (foundRefreshToken != null || foundRefreshToken.equals(inboundRefreshToken)) {
            UserDetails foundUser = customerService.findByEmailOrThrow(username);
            String accessToken = tokenService.generateAccessToken(foundUser);
            return new TokenResponseDto(accessToken);
        } else {
            return  new TokenResponseDto(null);
        }
    }
}
