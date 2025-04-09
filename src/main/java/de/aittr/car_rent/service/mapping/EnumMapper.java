package de.aittr.car_rent.service.mapping;

import de.aittr.car_rent.domain.entity.CarFuelType;
import de.aittr.car_rent.domain.entity.CarStatus;
import de.aittr.car_rent.domain.entity.CarTransmissionType;
import de.aittr.car_rent.domain.entity.CarType;
import org.mapstruct.Named;

public class EnumMapper {
    public static <T extends Enum<T>> T toEnumIgnoreCase(Class<T> enumType, String value) {
        if (value == null) return null;
        for (T constant : enumType.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(value)) {
                return constant;
            }
        }
        throw new IllegalArgumentException("No enum constant " + enumType.getCanonicalName() + " for value: " + value);
    }

    @Named("mapCarType")
    public static CarType mapCarType(String type) {
        return toEnumIgnoreCase(CarType.class, type);
    }

    @Named("mapCarFuelType")
    public static CarFuelType mapCarFuelType(String type) {
        return toEnumIgnoreCase(CarFuelType.class, type);
    }

    @Named("mapTransmissionType")
    public static CarTransmissionType mapTransmissionType(String type) {
        return toEnumIgnoreCase(CarTransmissionType.class, type);
    }


    @Named("mapCarStatus")
    public static CarStatus mapCarStatus(String type) {
        return toEnumIgnoreCase(CarStatus.class, type);
    }
}
