package de.aittr.car_rent.security.sec_dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;

    public TokenResponseDto(){}

    /**
     * Конструктор для отправки accessToken покупателю для получения refreshToken
     * @param accessToken
     */
    public TokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public TokenResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return String.format("Token Response Dto: access token - %s; refresh token - %s",
                accessToken, refreshToken);
    }

}
