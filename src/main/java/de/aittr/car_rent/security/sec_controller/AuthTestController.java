package de.aittr.car_rent.security.sec_controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/auth")
public class AuthTestController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String withAdminUser(@AuthenticationPrincipal Object principal) {
        return "admin";
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public String withCustomerUser(@AuthenticationPrincipal Object principal){
        return "customer";
    }

    @GetMapping("/any-authentication")
    @PreAuthorize("isAuthenticated()")
    public String withAnyAuthentication(@AuthenticationPrincipal Object principal){
        return "withAnyAuthentication";
    }

    @GetMapping("/not-secured")
    public String notSecured(){
        return "notSecured";
    }
}
