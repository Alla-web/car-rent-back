package de.aittr.car_rent.service.interfaces;

import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.domain.entity.Customer;

import java.util.List;

public interface CustomerService {

    CustomerResponseDto save(CustomerResponseDto customer);

    List<CustomerResponseDto> getAllActiveCustomers();

    Customer getActiveCustomerEntityById(Long id);

    void update(CustomerResponseDto customer);

    void deleteById(Long id);

    void deleteByName(String name);

    void restoreById(Long id);

    long getAllActiveCustomersNumber();
}
