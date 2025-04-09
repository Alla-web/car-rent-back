package de.aittr.car_rent.security.service;

import de.aittr.car_rent.domain.dto.CustomerRegisterDto;
import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.domain.entity.Customer;
import de.aittr.car_rent.domain.entity.Role;
import de.aittr.car_rent.exception_handling.exceptions.RestApiException;
import de.aittr.car_rent.security.dto.LoginRequestDto;
import de.aittr.car_rent.security.dto.TokenResponseDto;
import de.aittr.car_rent.service.CustomerRoleServiceImpl;
import de.aittr.car_rent.service.interfaces.CustomerService;
import de.aittr.car_rent.service.interfaces.EmailService;
import de.aittr.car_rent.service.mapping.CustomerMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Map<String, String> refreshStorage = new HashMap<>();

    private final CustomerService customerService;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomerMapper customerMapper;
    private final EmailService emailService;

    public CustomerResponseDto register(final CustomerRegisterDto registerDto) {
        final Role initCustomerRole = CustomerRoleServiceImpl.initCustomerRole;
        return register(registerDto, initCustomerRole);
    }

    public CustomerResponseDto register(final CustomerRegisterDto registerDto, final Role role) {
        final String normalizedEmail = registerDto.email().toLowerCase().trim();
        if (customerService.findByEmail(normalizedEmail).isPresent()) {
            throw new RestApiException("Customer already exists", HttpStatus.CONFLICT);
        }
        final String encodedPassword = passwordEncoder.encode(registerDto.password());
        final Customer registredCustomer = new Customer(registerDto.firstName(),
                registerDto.lastName(),
                encodedPassword,
                normalizedEmail,
                role);

        customerService.save(registredCustomer);
        emailService.sendConfirmationEmail(registredCustomer);
        return customerMapper.toDto(registredCustomer);
    }

    //метод первичной проверки пароля и логина и отдачи ему access и refresh токенов
    public TokenResponseDto login(final LoginRequestDto inboundCustomer) {
        final String username = inboundCustomer.email().toLowerCase().trim();
        final Optional<Customer> foundCustomer = customerService.findByEmail(username);

        if (foundCustomer.isEmpty()) {
            throw new RestApiException("Password or email incorrect", HttpStatus.FORBIDDEN);
        }

        Customer existingCustomer = foundCustomer.get();
        if(!existingCustomer.isActive()) {
            throw new RestApiException("Your profile is not active. Pleas confirm your email or contact the administrator", HttpStatus.FORBIDDEN);
        }
        if (passwordEncoder.matches(inboundCustomer.password(), existingCustomer.getPassword())) {
            final String accessToken = tokenService.generateAccessToken(existingCustomer);
            final String refreshToken = tokenService.generateRefreshToken(existingCustomer);
            refreshStorage.put(username, refreshToken);
            return new TokenResponseDto(accessToken, refreshToken);
        } else {
            throw new RestApiException("Password or email incorrect", HttpStatus.FORBIDDEN);
        }
    }

    //метод выдачи нового access-токена (когда он истек) на основании refresh токена
    public TokenResponseDto getNewAccessToken(String inboundRefreshToken) {

        Claims refreshClaims = tokenService.getRafreshClaims(inboundRefreshToken);
        String username = refreshClaims.getSubject();
        String foundRefreshToken = refreshStorage.get(username);
        if (StringUtils.isNoneBlank(foundRefreshToken) && foundRefreshToken.equals(inboundRefreshToken)) {
            UserDetails foundUser = customerService.findByEmailOrThrow(username);
            String accessToken = tokenService.generateAccessToken(foundUser);
            return new TokenResponseDto(accessToken);
        } else {
            return new TokenResponseDto(null);
        }
    }
}
