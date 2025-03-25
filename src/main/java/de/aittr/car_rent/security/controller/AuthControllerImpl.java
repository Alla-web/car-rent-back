package de.aittr.car_rent.security.controller;

import de.aittr.car_rent.domain.dto.CustomerRegisterDto;
import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.security.dto.LoginRequestDto;
import de.aittr.car_rent.security.dto.RefreshRequestDto;
import de.aittr.car_rent.security.dto.TokenResponseDto;
import de.aittr.car_rent.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService service;

    @Override
    public CustomerResponseDto register(CustomerRegisterDto registerDto) {
        return service.register(registerDto);
    }

    @Override
    public TokenResponseDto login(LoginRequestDto loginDto) {
        return service.login(loginDto);
    }

    @Override
    public TokenResponseDto getNewAccessToken(RefreshRequestDto refreshRequestDto) {
        return service.getNewAccessToken(refreshRequestDto.getRefreshToken());
    }
}
