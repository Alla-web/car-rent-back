package de.aittr.car_rent.repository;

import de.aittr.car_rent.domain.dto.CarResponseDto;
import de.aittr.car_rent.domain.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {


    //методы, которых нет в спике CRUD-операций JpaRepository

    //найти авто по бренду
    List<CarResponseDto> getCarsByBrand(String brand);

    //найти авто по модели
    List<CarResponseDto> getCarsByModel(String model);

    //найти авто по году выпуска
    List<CarResponseDto> getCarsByYear(int year);

    //найти авто по типу кузова
    List<CarResponseDto> getCarsByType(CarType type);

    //найти авто по типу топлива
    List<CarResponseDto> getCarsByFuelType(CarFuelType fuelType);

    //найти авто по типу коробки передач
    List<CarResponseDto> getCarsByTransmissionType(CarTransmissionType transmissionType);

    //найти авто по статусу авто (доступно, арендовано, в ремонте, снято с аренды)
    List<CarResponseDto> getCarsByCarStatus(CarStatus carStatus);

    //найти авто по стоимости аренды в день
    List<CarResponseDto> getCarsByDayRentalPrice(BigDecimal dayRentalPrice);
}
