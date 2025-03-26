package de.aittr.car_rent.security.controller;

import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.domain.entity.Customer;
import de.aittr.car_rent.service.interfaces.CustomerService;
import de.aittr.car_rent.service.mapping.CustomerMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RestController
@RequestMapping("/test/auth")
@RequiredArgsConstructor
public class AuthTestControllerImpl implements AuthTestController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public CustomerResponseDto withAdminUser(@AuthenticationPrincipal
                                             @Parameter(hidden = true)
                                             String authUserEmail) {
        Customer foundCustomer = customerService.findByEmailOrThrow(authUserEmail);
        return customerMapper.toDto(foundCustomer);
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public CustomerResponseDto withCustomerUser(@AuthenticationPrincipal
                                                @Parameter(hidden = true)
                                                String authUserEmail) {
        Customer foundCustomer = customerService.findByEmailOrThrow(authUserEmail);
        return customerMapper.toDto(foundCustomer);
    }

    @GetMapping("/any-authentication")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public CustomerResponseDto withAnyAuthentication(@AuthenticationPrincipal
                                                     @Parameter(hidden = true)
                                                     String authUserEmail) {
        Customer foundCustomer = customerService.findByEmailOrThrow(authUserEmail);
        return customerMapper.toDto(foundCustomer);
    }

    @GetMapping("/not-secured")
    @Override
    public String notSecured() {
        return "Endpoint not secured";
    }
}
