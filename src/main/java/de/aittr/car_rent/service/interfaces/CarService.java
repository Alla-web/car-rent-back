package de.aittr.car_rent.service.interfaces;

import de.aittr.car_rent.domain.dto.CarResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface CarService {

    //сохранить продукт в базе данных (сохраненяется авто со статуслм активный)
    CarResponseDto saveCar(CarResponseDto carDto);

    //получить все авто (активные)
    List<CarResponseDto> getAllCars();

    //найти авто по его id
    CarResponseDto getCarById(Long id);



    //найти авто по бренду
    List<CarResponseDto> getCarsByBrand(String brand);

    //найти авто по модели
    List<CarResponseDto> getCarsByModel(String model);

    //найти авто по году выпуска
    List<CarResponseDto> getCarsByYear(int year);

    //найти авто по типу кузова
    List<CarResponseDto> getCarsByType(String type);

    //найти авто по типу топлива
    List<CarResponseDto> getCarsByFuelType(String fuelType);

    //найти авто по типу коробки передач
    List<CarResponseDto> getCarsByTransmissionType(String transmissionType);

    //найти авто по статусу авто (доступно, арендовано, в ремонте, снято с аренды)
    List<CarResponseDto> getCarsByCarStatus(String carStatus);

    //найти авто по стоимости аренды в день
    List<CarResponseDto> getCarsByDayRentalPrice(BigDecimal minDayRentalPrice, BigDecimal maxDayRentalPrice);



    //изменить авто по его id
    void updateCar(CarResponseDto carDto);

    //удалить авто по его id
    void deleteCarById(Long id);

    //восстановить авто по его id

    //добавить ссылку на изображение авто по id авто
    void attachImageToCar(Long id, String imageUrl);

}
