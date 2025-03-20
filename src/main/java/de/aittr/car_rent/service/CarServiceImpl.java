package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.dto.CarResponseDto;
import de.aittr.car_rent.domain.entity.Car;
import de.aittr.car_rent.exception_handling.exceptions.CarNotFoundException;
import de.aittr.car_rent.repository.CarRepository;
import de.aittr.car_rent.service.interfaces.CarService;
import de.aittr.car_rent.service.mapping.CarMappingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMappingService carMappingService;
    private final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    @Override
    public CarResponseDto saveCar(CarResponseDto carDto) {
        try {
            Car entity = carMappingService.mapDtoToEntity(carDto);
            entity = carRepository.save(entity);
            return carMappingService.mapEntityToDto(entity);
        } catch (Exception e) {
            //TODO дописать исключения по валидации полей объекта авто
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CarResponseDto> getAllCars() {
        return carRepository.findAll()
                .stream()
                .filter(Car::isActive)
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public CarResponseDto getCarById(Long id) {
        return carMappingService.mapEntityToDto(getCarEntityById(id));
    }

    private Car getCarEntityById(Long id) {
        return carRepository.findById(id).orElseThrow(() -> new CarNotFoundException(id));
    }

    @Override
    public List<CarResponseDto> getCarsByBrand(String brand) {
        return carRepository.findAll()
                .stream()
                //TODO нужно ли использовать обрезку пробелов или это фронт сделает?
                .filter(car -> car.getBrand().equals(brand))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByModel(String model) {
        return carRepository.findAll()
                .stream()
                //TODO нужно ли использовать обрезку пробелов или это фронт сделает?
                .filter(car -> car.getModel().equals(model))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByYear(int year) {
        return carRepository.findAll()
                .stream()
                //TODO нужно ли использовать обрезку пробелов или это фронт сделает?
                .filter(car -> car.getYear() == year)
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByType(String type) {
        return carRepository.findAll()
                .stream()
                //TODO нужно ли использовать обрезку пробелов или это фронт сделает?
                .filter(car->car.getType().equals(type))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByFuelType(String fuelType) {
        return carRepository.findAll()
                .stream()
                //TODO нужно ли использовать обрезку пробелов или это фронт сделает?
                .filter(car->car.getFuelType().equals(fuelType))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByTransmissionType(String transmissionType) {
        return carRepository.findAll()
                .stream()
                //TODO нужно ли использовать обрезку пробелов или это фронт сделает?
                .filter(car -> car.getTransmissionType().equals(transmissionType))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByCarStatus(String carStatus) {
        return carRepository.findAll()
                .stream()
                .filter(car -> car.getCarStatus().equals(carStatus))
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public List<CarResponseDto> getCarsByDayRentalPrice(BigDecimal minDayRentalPrice, BigDecimal maxDayRentalPrice) {
        return carRepository.findAll()
                .stream()
                .filter(car-> car.getDayRentalPrice().compareTo(minDayRentalPrice) >= 0 &&
                        car.getDayRentalPrice().compareTo(maxDayRentalPrice) <= 0)
                .map(carMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public void updateCar(CarResponseDto carDto) {
        Long id = carDto.id();
        Car existCar = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        //TODO нужно ли ещё что-то обновлять?
        existCar.setDayRentalPrice(carDto.dayRentalPrice());
        existCar.setCarStatus(carDto.carStatus());
    }

    @Override
    public void deleteCarById(Long id) {
        try {
            carRepository.deleteById(id);
        } catch (Exception e) {
            throw new CarNotFoundException(id);
        }
    }

    @Override
    public void attachImageToCar(Long id, String imageUrl) {
        //TODO доделать после контроллера
    }
}
