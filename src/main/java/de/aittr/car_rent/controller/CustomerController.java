package de.aittr.car_rent.controller;

import de.aittr.car_rent.domain.dto.BookingDto;
import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.domain.dto.CustomerUpdateRequestDto;
import de.aittr.car_rent.domain.entity.Booking;
import de.aittr.car_rent.service.interfaces.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Customer controller", description = "Controller for various operations with customers")
public class CustomerController {

    private final CustomerService customerService;

    //GET -> http://localhost:8080/customers/all
    @GetMapping("/all")
    @Operation(description = "Shows all active customers")
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN'})")
    @SecurityRequirement(name = "bearerAuth")
    public List<CustomerResponseDto> getAllActiveCustomers() {
        return customerService.getAllActiveCustomers();
    }

    //GET -> http://localhost:8080/customers/{id}
    @GetMapping("/{id}")
    @Operation(description = "Finds customer by id")
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN'})")
    @SecurityRequirement(name = "bearerAuth")
    public CustomerResponseDto getActiveCustomerById(@PathVariable Long id) {
        return customerService.getActiveCustomerById(id);
    }

    //PUT -> http://localhost:8080/customers/{id}/update
    @PutMapping("/{id}/update")
    @Operation(description = "Update customer with new data")
    @PreAuthorize("isAuthenticated()")
    //todo обновление на основе авторизации. Пользователь не должен иметь возможность обновлять любого другого пользователя!
    @SecurityRequirement(name = "bearerAuth")
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
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN'})")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteById(@PathVariable Long id) {
        customerService.deleteById(id);
    }

    //PUT -> http://localhost:8080/customers/1/restore
    @PutMapping("/{id}/restore")
    @Operation(description = "Restores customer by id")
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN'})")
    @SecurityRequirement(name = "bearerAuth")
    public void restoreById(@PathVariable Long id) {
        customerService.restoreById(id);
    }

    //GET -> http://localhost:8080/customers/1/all-bookings
    @GetMapping("{id}/all-bookings")
    @Operation(description = "Finds all bookings by customer id")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public List<BookingDto> getAllBookingsByCustomerId(@PathVariable Long id) {
        return customerService.getAllBookingsByCustomerId(id);
    }

    //TODO not working
    //GET -> http://localhost:8080/customers/{email}/all-bookings
    @GetMapping("/all-bookings/{email}")
    @Operation(description = "Finds all bookings by customer email")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public List<BookingDto> getAllBookingsByCustomerEmail(String email){
        return customerService.getAllBookingsByCustomerEmail(email);
    }
}
