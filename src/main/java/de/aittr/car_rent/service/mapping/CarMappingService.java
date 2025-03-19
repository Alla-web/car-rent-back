package de.aittr.car_rent.service.mapping;

import de.aittr.car_rent.domain.dto.CarResponseDto;
import de.aittr.car_rent.domain.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMappingService {

    CarResponseDto mapEntityToDto(Car carEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    Car mapDtoToEntity(CarResponseDto carDto);

}
