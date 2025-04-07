package de.aittr.car_rent.security.controller;

import de.aittr.car_rent.domain.dto.CustomerRegisterDto;
import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.security.dto.LoginRequestDto;
import de.aittr.car_rent.security.dto.RefreshRequestDto;
import de.aittr.car_rent.security.dto.TokenResponseDto;
import de.aittr.car_rent.security.service.AuthService;
import de.aittr.car_rent.service.interfaces.ConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService service;
    private final ConfirmationService confirmationService;

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

    @Override
    public ResponseEntity<String> confirmRegistration(@PathVariable String code) {
        confirmationService.confirmRegistration(code);
        return ResponseEntity.ok("User successfully activated!");
    }
}
