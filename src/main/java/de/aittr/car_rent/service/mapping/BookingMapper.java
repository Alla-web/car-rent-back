package de.aittr.car_rent.service.mapping;

import de.aittr.car_rent.domain.dto.BookingRequestDto;
import de.aittr.car_rent.domain.dto.BookingResponseDto;
import de.aittr.car_rent.domain.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "carStatus", source = "car.carStatus")
    @Mapping(target = "updateBookingDate", source = "updateBookingDate")
    @Mapping(target = "bookingStatus", source = "bookingStatus")
    BookingResponseDto mapEntityToDto(Booking entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    @Mapping(target = "createBookingDate", ignore = true)
    @Mapping(target = "updateBookingDate", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    Booking mapDtoToEntity(BookingRequestDto dto);


}
