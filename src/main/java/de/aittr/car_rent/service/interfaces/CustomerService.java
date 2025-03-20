package de.aittr.car_rent.service.interfaces;

import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.domain.entity.Booking;
import de.aittr.car_rent.domain.entity.Customer;

import java.util.List;

public interface CustomerService {

    CustomerResponseDto save(CustomerResponseDto customer);

    List<CustomerResponseDto> getAllActiveCustomers();

    Customer getActiveCustomerEntityById(Long id);

    CustomerResponseDto getActiveCustomerById(Long id);

    void update(CustomerResponseDto customer);

    void deleteById(Long id);

    void deleteByFirstName(String firstName);

    void deleteByLastName(String lastName);

    void restoreById(Long id);

    List<Booking> getAllBookingsByCustomerId(Long customerId);
}
