package de.aittr.car_rent.security.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class RefreshRequestDto {

    private String refreshToken;

    @Override
    public String toString() {
        return String.format("Refresh Request Dto: refresh token - %s",
                refreshToken);
    }
}
