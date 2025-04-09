package de.aittr.car_rent.service.mapping;

import de.aittr.car_rent.domain.dto.CarResponseDto;
import de.aittr.car_rent.domain.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = EnumMapper.class)
public interface CarMappingService {

    @Mapping(target = "isActive", source = "active")
    CarResponseDto mapEntityToDto(Car carEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", source = "type", qualifiedByName = "mapCarType")
    @Mapping(target = "fuelType", source = "fuelType", qualifiedByName = "mapCarFuelType")
    @Mapping(target = "transmissionType", source = "transmissionType", qualifiedByName = "mapTransmissionType")
    @Mapping(target = "carStatus", source = "carStatus", qualifiedByName = "mapCarStatus")
    @Mapping(target = "active", constant = "true")
    Car mapDtoToEntity(CarResponseDto carDto);

}
