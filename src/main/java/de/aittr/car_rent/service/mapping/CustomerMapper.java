package de.aittr.car_rent.service.mapping;

import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.domain.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "isAdult", source = "adult")
    CustomerResponseDto toDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    Customer toEntity(CustomerResponseDto customerResponseDto);

}
