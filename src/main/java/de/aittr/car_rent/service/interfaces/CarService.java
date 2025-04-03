package de.aittr.car_rent.service.interfaces;

import de.aittr.car_rent.domain.dto.CarResponseDto;
import de.aittr.car_rent.domain.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CarService {

    /**
     * Метод сохраняет авто в базе данных (сохраненяется авто со статуслм активный)
     *
     * @param carDto со всеми свойствами
     * @return CarResponseDto сохранённой машины
     */
    CarResponseDto saveCar(CarResponseDto carDto);

    /**
     * Метод получения всех активных (существующих физически) авто, имеющихся в БД
     *
     * @return список активных авто
     */
    List<CarResponseDto> getAllCars();

    /**
     * Метод получения всех существующих в БД авто
     *
     * @return список всех авто, существующих в БД
     */
    List<CarResponseDto> getAllCarsToAdmin();

    /**
     * Метод нахождения авто по его id
     *
     * @param id авто
     * @return CarResponseDto найденной машины
     */
    CarResponseDto getCarById(Long id);

    /**
     * Метод нахождения машины по её id. Если машина не найдена, будет выброшено исключение
     *
     * @param id машины
     * @return entity найденной машины
     */
    Car getOrThrow(Long id);

    /**
     * Метод нахождения авто по их бренду. Если машины не найдена, будет возвращён пустой список
     *
     * @param brand авто
     * @return список CarResponseDto машин переданного в метод бренда
     */
    List<CarResponseDto> getCarsByBrand(String brand);

    /**
     * Метод нахождения авто по их модели. Если машины не найдена, будет возвращён пустой список
     *
     * @param model авто
     * @return список CarResponseDto машин переданной в метод модели
     */
    List<CarResponseDto> getCarsByModel(String model);

    /**
     * Метод нахождения авто по году выпуска. Если машины не найдена, будет возвращён пустой список
     *
     * @param year авто
     * @return список CarResponseDto машин переданного в метод года выпуска
     */
    List<CarResponseDto> getCarsByYear(int year);

    /**
     * Метод нахождения авто по типу кузова. Если машины не найдена, будет возвращён пустой список
     *
     * @param type кузова авто
     * @return список CarResponseDto машин переданного в метод типа кузова
     */
    List<CarResponseDto> getCarsByType(CarType type);

    /**
     * Метод нахождения авто по типу топлива. Если машины не найдена, будет возвращён пустой список
     *
     * @param fuelType авто
     * @return список CarResponseDto машин переданного в метод типа топлива
     */
    List<CarResponseDto> getCarsByFuelType(CarFuelType fuelType);

    /**
     * Метод нахождения авто по типу коробки передач. Если машины не найдена, будет возвращён пустой список
     *
     * @param transmissionType авто
     * @return список CarResponseDto машин переданного в метод типа коробки передач
     */
    List<CarResponseDto> getCarsByTransmissionType(CarTransmissionType transmissionType);

    /**
     * Метод нахождения авто по статусу авто (доступно, арендовано, в ремонте, снято с аренды). Если машины не найдена, будет возвращён пустой список
     *
     * @param carStatus авто
     * @return список CarResponseDto машин c переданным в метод статусом
     */
    List<CarResponseDto> getCarsByCarStatus(CarStatus carStatus);

    /**
     * Метод нахождения авто по стоимости их аренды в день. Если машины не найдена, будет возвращён пустой список
     *
     * @param minDayRentalPrice
     * @param maxDayRentalPrice
     * @return список CarResponseDto машин c арендой в день, попадающей в переданные в метод пределы включительно
     */
    List<CarResponseDto> getCarsByDayRentalPrice(BigDecimal minDayRentalPrice, BigDecimal maxDayRentalPrice);

    /**
     * Метод проверяет, свободна ли машина в промежутке времени между указанными датами
     *
     * @param carId - id авто, доступность которого проверяется
     * @param from  - дата начала периода проверки доступности авто
     * @param to    - дата окончания периода проверки доступности авто
     * @return - true/false в зависимости от результата проверки
     */
    boolean checkIfCarAvailableByDates(
            Long carId,
            LocalDateTime from,
            LocalDateTime to);

    /**
     * Метод изменения статуса и стоимости аренды в день авто. Если машина не найдена метод выбросит исключение
     *
     * @param carDto
     */
    void updateCar(CarResponseDto carDto);

    /**
     * Метод удаления авто из спискка доступных пользователю. Если машина не найдена метод выбросит исключение
     *
     * @param id
     */
    CarResponseDto deleteCarById(Long id);

    /**
     * Метод восстанавливает ранее удалённое авто в БД
     *
     * @param carId
     * @return car info DTO
     */
    CarResponseDto restoreCar(Long carId);

    /**
     * Метод прикрепления фото к авто
     *
     * @param id
     * @param file
     * @return
     */
    @Transactional
    String attachImageToCar(Long id, MultipartFile file);

    List<CarResponseDto> getAllAvailableCarsByDates(LocalDateTime startDate, LocalDateTime endDate);

    List<CarResponseDto> filterAvailableCars(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            List<String> brands,
            List<String> fuelTypes,
            List<String> transmissionTypes,
            BigDecimal minPrice,
            BigDecimal maxPrice);

    List<String> getAllAvailableBrands();

    public List<String> getAllCarTypes();

}
