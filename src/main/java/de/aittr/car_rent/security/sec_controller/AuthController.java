package de.aittr.car_rent.security.sec_controller;

import de.aittr.car_rent.security.sec_dto.LoginRequestDto;
import de.aittr.car_rent.security.sec_dto.RefreshRequestDto;
import de.aittr.car_rent.security.sec_dto.TokenResponseDto;
import de.aittr.car_rent.security.sec_service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public TokenResponseDto login(
            @RequestBody LoginRequestDto loginDto) {
            return service.login(loginDto);
    }

    public TokenResponseDto getNewAccessToken(
            @RequestBody RefreshRequestDto refreshRequestDto) {
        return service.getNewAccessToken(refreshRequestDto.getRefreshToken());
    }
}
