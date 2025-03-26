package de.aittr.car_rent.service.interfaces;

import de.aittr.car_rent.domain.dto.BookingDto;
import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.domain.dto.CustomerUpdateRequestDto;
import de.aittr.car_rent.domain.entity.Booking;
import de.aittr.car_rent.domain.entity.Customer;
import de.aittr.car_rent.exception_handling.exceptions.CustomerNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с клиентами.
 * <p>
 * Данный сервис реализует интерфейс {@link CustomerService} и предоставляет методы для:
 * <ul>
 *   <li>Получения всех активных клиентов;</li>
 *   <li>Получения активного клиента по идентификатору;</li>
 *   <li>Обновления данных клиента;</li>
 *   <li>Деактивации и восстановления клиента;</li>
 *   <li>Получения списка бронирований клиента;</li>
 *   <li>Поиска клиента по email для аутентификации.</li>
 * </ul>
 * </p>
 */
public interface CustomerService {

    /**
     * Возвращает список всех активных клиентов.
     *
     * @return список {@link CustomerResponseDto} активных клиентов.
     */
    List<CustomerResponseDto> getAllActiveCustomers();

    /**
     * Возвращает активного клиента по идентификатору.
     *
     * @param id идентификатор клиента.
     * @return {@link CustomerResponseDto} клиента.
     * @throws CustomerNotFoundException если клиент не найден или не активен.
     */
    CustomerResponseDto getActiveCustomerById(Long id);

    /**
     * Возвращает объект клиента, если он активен, иначе выбрасывает исключение.
     *
     * @param id идентификатор клиента.
     * @return активный объект {@link Customer}.
     * @throws CustomerNotFoundException если клиент не найден или не активен.
     */
    Customer getActiveCustomerEntityById(Long id);

    /**
     * Обновляет данные клиента.
     * <p>
     * Обновление включает изменение имени, фамилии и email.
     * </p>
     *
     * @param updateDto  объект с новыми данными клиента.
     * @param customerId идентификатор клиента, который требуется обновить.
     * @return обновленный {@link CustomerResponseDto} клиента.
     * @throws CustomerNotFoundException если клиент не найден.
     */
    CustomerResponseDto update(CustomerUpdateRequestDto updateDto, long customerId);

    /**
     * Деактивирует клиента, устанавливая флаг активности в false.
     *
     * @param id идентификатор клиента, которого необходимо деактивировать.
     * @throws CustomerNotFoundException если клиент не найден.
     */
    void deleteById(Long id);

    /**
     * Восстанавливает клиента, устанавливая флаг активности в true.
     *
     * @param id идентификатор клиента, которого необходимо восстановить.
     * @throws CustomerNotFoundException если клиент не найден.
     */
    void restoreById(Long id);

    /**
     * Возвращает список бронирований, принадлежащих клиенту.
     *
     * @param customerId идентификатор клиента.
     * @return список объектов {@link Booking} клиента.
     * @throws CustomerNotFoundException если клиент не найден.
     */
    List<BookingDto> getAllBookingsByCustomerId(Long customerId);

    /**
     * Находит все букинги клиента по email
     * @param email
     * @return List<BookingDto>
     */
    List<BookingDto> getAllBookingsByCustomerEmail(String email);

    /**
     * Возвращает объект клиента по его идентификатору или выбрасывает исключение,
     * если клиент не найден.
     *
     * @param customerId идентификатор клиента.
     * @return объект {@link Customer}.
     * @throws CustomerNotFoundException если клиент с указанным идентификатором не найден.
     */
    Customer getOrThrow(Long customerId);

    /**
     * Находит клиента по email или выбрасывает исключение, если клиент не найден.
     * <p>
     * Метод используется для получения клиента, который будет использоваться в аутентификации и авторизации.
     * </p>
     *
     * @param email email клиента.
     * @return объект {@link Customer}.
     * @throws CustomerNotFoundException если клиент с указанным email не найден.
     */
    Customer findByEmailOrThrow(String email);

    /**
     * Находит клиента по email.
     *
     * @param email email клиента.
     * @return {@link Optional} содержащий найденного клиента, либо пустой, если клиент не найден.
     */
    Optional<Customer> findByEmail(String email);

    Customer save(Customer customer);
}
