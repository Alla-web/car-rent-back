package de.aittr.car_rent.controller;

import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.domain.dto.CustomerUpdateRequestDto;
import de.aittr.car_rent.domain.entity.Booking;
import de.aittr.car_rent.domain.entity.Customer;
import de.aittr.car_rent.service.interfaces.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Customer controller", description = "Controller for various operations with customers")
public class CustomerController {

    private final CustomerService customerService;

    // POST -> http://localhost:8080/customers/add
    @PostMapping("/add")
    @Operation(description = "Saves new customer do the database")
    public CustomerResponseDto save(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Instance of a Customer")
            CustomerResponseDto customer) {
        return customerService.save(customer);
    }

    //GET -> http://localhost:8080/customers/all
    @GetMapping("/all")
    @Operation(description = "Shows all active customers")
    public List<CustomerResponseDto> getAllActiveCustomers() {
        return customerService.getAllActiveCustomers();
    }

    //GET -> http://localhost:8080/customers/{id}
    @GetMapping("/{id}")
    @Operation(description = "Finds customer by id")
    public CustomerResponseDto getActiveCustomerById(@PathVariable Long id) {
        return customerService.getActiveCustomerById(id);
    }

    //PUT -> http://localhost:8080/customers/{id}/update
    @PutMapping("/{id}/update")
    @Operation(description = "Update customer with new data")
    public CustomerResponseDto update(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Instance of a Customer")
            CustomerUpdateRequestDto updateDto,
            @PathVariable Long id) {
        return customerService.update(updateDto, id);
    }

    //  DELETE ->  http://localhost:8080/customers/1/delete
    @DeleteMapping("{id}/delete")
    @Operation(description = "Delete customer from the database")
    public void deleteById(@PathVariable Long id) {
        customerService.deleteById(id);
    }

    //PUT -> http://localhost:8080/customers/1/restore
    @PutMapping("/{id}/restore")
    @Operation(description = "Restores customer by id")
    public void restoreById(@PathVariable Long id) {
        customerService.restoreById(id);
    }

    //GET -> http://localhost:8080/customers/1/all-bookings
    @GetMapping("{id}/all-bookings")
    @Operation(description = "Finds all bookings by customer id")
    public List<Booking> getAllBookingsByCustomerId(@PathVariable Long id) {
        return customerService.getAllBookingsByCustomerId(id);
    }
}
