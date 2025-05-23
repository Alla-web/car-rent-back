package de.aittr.car_rent.service;


import de.aittr.car_rent.domain.dto.BookingResponseDto;
import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.domain.dto.CustomerUpdateRequestDto;
import de.aittr.car_rent.domain.entity.Customer;
import de.aittr.car_rent.exception_handling.exceptions.CustomerNotFoundException;
import de.aittr.car_rent.repository.CustomerRepository;
import de.aittr.car_rent.service.interfaces.CustomerService;
import de.aittr.car_rent.service.mapping.BookingMapper;
import de.aittr.car_rent.service.mapping.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper customerMapper;
    private final BookingMapper bookingMapper;

    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        return repository
                .findAll()
                .stream()
                .map(customerMapper::toDto)
                .toList();
    }

    @Override
    public CustomerResponseDto getActiveCustomerById(Long id) {
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
    public CustomerResponseDto update(CustomerUpdateRequestDto updateDto, long customerId) {
        Customer customer = repository
                .findById(customerId)
                .orElseThrow(CustomerNotFoundException::new);
        customer.setFirstName(updateDto.firstName());
        customer.setLastName(updateDto.lastName());
        customer.setEmail(updateDto.email());
        return customerMapper.toDto(repository.save(customer));
    }

    @Override
    public void deleteById(Long id) {
        Customer customer = getOrThrow(id);
        customer.setActive(false);
        repository.save(customer);
    }

    @Override
    public void restoreById(Long id) {
        Customer customer = getOrThrow(id);
        customer.setActive(true);
        repository.save(customer);
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByCustomerId(Long customerId) {
        return getOrThrow(customerId)
                .getBookings()
                .stream()
                .map(bookingMapper::mapEntityToDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByCustomerEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(CustomerNotFoundException::new)
                .getBookings()
                .stream()
                .map(bookingMapper::mapEntityToDto)
                .toList();
    }

    @Override
    public Customer getOrThrow(Long customerId) {
        return repository
                .findById(customerId)
                .orElseThrow(CustomerNotFoundException::new);
    }

    @Override
    public Customer findByEmailOrThrow(String email) {
        return findByEmail(email).orElseThrow(CustomerNotFoundException::new);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public CustomerResponseDto save(Customer customer) {
        return customerMapper.toDto((repository.save(customer)));
    }

    @Override
    public CustomerResponseDto findByToken(String email) {
        return customerMapper.toDto(findByEmailOrThrow(email));
    }
}
