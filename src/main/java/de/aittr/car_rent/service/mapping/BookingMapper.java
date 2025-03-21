package de.aittr.car_rent.service.mapping;

import de.aittr.car_rent.domain.dto.BookingDto;
import de.aittr.car_rent.domain.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "customerId", source = "customer.id")
    BookingDto mapEntityToDto(Booking entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    Booking mapDtoToEntity(BookingDto dto);
}
