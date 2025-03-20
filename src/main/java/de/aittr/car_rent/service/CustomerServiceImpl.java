package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.domain.entity.Customer;
import de.aittr.car_rent.exception_handling.exceptions.CustomerNotFoundException;
import de.aittr.car_rent.repository.CustomerRepository;
import de.aittr.car_rent.service.interfaces.CustomerService;
import de.aittr.car_rent.service.mapping.CustomerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper customerMapper;
    //TODO add booking to constructor
//    private final BookingService bookingService;


    @Override
    @Transactional
    public CustomerResponseDto save(CustomerResponseDto customer) {
        Customer entity = customerMapper.toEntity(customer);
        entity = repository.save(entity);
        return customerMapper.toDto(entity);
    }

    @Override
    public List<CustomerResponseDto> getAllActiveCustomers() {

        return List.of();
    }

    @Override
    public Customer getActiveCustomerEntityById(Long id) {
        return repository
                .findById(id)
                .filter(Customer::isActive)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    public void update(CustomerResponseDto customer) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByName(String name) {

    }

    @Override
    public void restoreById(Long id) {

    }

    @Override
    public long getAllActiveCustomersNumber() {
        return 0;
    }
}
