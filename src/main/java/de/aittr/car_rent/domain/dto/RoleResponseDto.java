package de.aittr.car_rent.domain.dto;

import de.aittr.car_rent.domain.entity.RoleTitle;
import io.swagger.v3.oas.annotations.media.Schema;

public record RoleResponseDto(

        @Schema(
                description = "Role unique identifier",
                example = "123",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Long id,

        @Schema(
                description = "Customer role",
                example = "Customer",
                allowableValues = {"CUSTOMER", "ADMIN"})
        RoleTitle title
) {
}
