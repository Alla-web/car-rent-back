package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.domain.entity.Booking;
import de.aittr.car_rent.domain.entity.Customer;
import de.aittr.car_rent.exception_handling.exceptions.CustomerNotFoundException;
import de.aittr.car_rent.repository.CustomerRepository;
import de.aittr.car_rent.service.interfaces.BookingService;
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

    @Override
    @Transactional
    public CustomerResponseDto save(CustomerResponseDto customer) {
        try{
            Customer entity = customerMapper.toEntity(customer);
            entity = repository.save(entity);
            return customerMapper.toDto(entity);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CustomerResponseDto> getAllActiveCustomers() {
        return repository
                .findAll()
                .stream()
                .filter(Customer::isActive)
                .map(customerMapper::toDto)
                .toList();
    }

    @Override
    public CustomerResponseDto getActiveCustomerById(Long id){
        return customerMapper.toDto(getActiveCustomerEntityById(id));
    }

    @Override
    public Customer getActiveCustomerEntityById(Long id) {
        return repository
                .findById(id)
                .filter(Customer::isActive)
                .orElseThrow(CustomerNotFoundException::new);
    }

    @Override
    public void update(CustomerResponseDto dtoCustomer) {
        Long id= dtoCustomer.id();
        Customer customer = repository.findById(id).orElseThrow(CustomerNotFoundException::new);
        customer.setAdult(dtoCustomer.isAdult());
        customer.setEmail(dtoCustomer.email());
        customer.setFirstName(dtoCustomer.firstName());
        customer.setLastName(dtoCustomer.lastName());
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.findById(id).
                    ifPresent(customer -> {
                        customer.setActive(false);});
        }catch (Exception e){
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public void deleteByFirstName(String firstName) {
        try {
            repository
                    .findByFirstName(firstName)
                    .ifPresent(customer -> {
                        customer.setActive(false);});
        }catch (Exception e){
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public void deleteByLastName(String lastName) {
        try {
            repository
                    .findByFirstName(lastName)
                    .ifPresent(customer -> {
                        customer.setActive(false);});
        }catch (Exception e){
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public void restoreById(Long id) {
        try {
            repository
                    .findById(id)
                    .ifPresent(customer -> {
                        customer.setActive(true);
                    });
        } catch (Exception e){
            throw new CustomerNotFoundException();
        }

    }

    @Override
    public List<Booking> getAllBookingsByCustomerId(Long customerId){
        return repository
                .findById(customerId)
                .get()
                .getBookings()
                .stream()
                .toList();
    }
}
