package de.aittr.car_rent.service.interfaces;

import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.domain.dto.CustomerUpdateRequestDto;
import de.aittr.car_rent.domain.entity.Booking;
import de.aittr.car_rent.domain.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    CustomerResponseDto save(CustomerResponseDto customer);

    List<CustomerResponseDto> getAllActiveCustomers();

    Customer getActiveCustomerEntityById(Long id);

    CustomerResponseDto getActiveCustomerById(Long id);

    CustomerResponseDto update(CustomerUpdateRequestDto updateDto, long customerId);

    void deleteById(Long id);

    void restoreById(Long id);

    List<Booking> getAllBookingsByCustomerId(Long customerId);

    Customer findByEmailOrThrow(String email);
    Optional<Customer> findByEmail(String email);
}
