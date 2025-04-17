package de.aittr.car_rent.security.controller;

import de.aittr.car_rent.domain.dto.CustomerRegisterDto;
import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.exception_handling.ErrorResponseDto;
import de.aittr.car_rent.exception_handling.ValidationErrorResponse;
import de.aittr.car_rent.security.dto.LoginRequestDto;
import de.aittr.car_rent.security.dto.RefreshRequestDto;
import de.aittr.car_rent.security.dto.TokenResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Authentication controller")
public interface AuthController {

    @Operation(
            summary = "Register a new customer",
            description = "Registers a new customer using provided registration details. " +
                    "On success, returns the registered customer's data. " +
                    "If the email already exists, a 409 Conflict error is returned; " +
                    "if validation fails, a 400 Bad Request error is returned with detailed error information."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer successfully registered",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomerResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Conflict. Customer already exists.",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Customer already exists",
                                    value = """
                                            {
                                              "message": "Customer already exists"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request. Detailed validation errors.",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Validation Error - first name",
                                            value = """
                                                    {
                                                      "message": "Validation failed",
                                                      "errors": {
                                                        "firstName": {
                                                          "message": "Customer first name must not be blank",
                                                          "rejectedValue": ""
                                                        }
                                                      }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Validation Error - email",
                                            value = """
                                                    {
                                                      "message": "Validation failed",
                                                      "errors": {
                                                        "email": {
                                                          "message": "Customer email must not be blank",
                                                          "rejectedValue": ""
                                                        }
                                                      }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Validation Error Example",
                                            value = """
                                                    {
                                                      "message": "Validation failed",
                                                      "errors": {
                                                        "firstName": {
                                                          "message": "Customer first name must not be blank",
                                                          "rejectedValue": ""
                                                        },
                                                        "lastName": {
                                                          "message": "Customer last name must not be blank",
                                                          "rejectedValue": ""
                                                        },
                                                        "email": {
                                                          "message": "Customer email must not be blank",
                                                          "rejectedValue": null
                                                        }
                                                      }
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    CustomerResponseDto register(@RequestBody
                                 @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                         description = "Customer registration details",
                                         required = true,
                                         content = @Content(
                                                 mediaType = "application/json",
                                                 schema = @Schema(implementation = CustomerRegisterDto.class)
                                         )
                                 )
                                 @Valid
                                 CustomerRegisterDto registerDto);


    @Operation(
            summary = "Customer authentication",
            description = "Authenticates the customer using the provided credentials" +
                    "In Swagger UI you can choose one of the predefined examples"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer successfully authenticated",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TokenResponseDto.class)
                    )),
            @ApiResponse(responseCode = "401", description = "Incorrect credentials",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )),
    })

    @PostMapping("/login")
    TokenResponseDto login(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication data. Select one of the predefined users or enter the data manually",
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoginRequestDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Customer 1",
                                            summary = "Customer-user 1 (role ROLE_CUSTOMER)",
                                            value = """
                                                    {
                                                      "email": "customer_1@car-rent.de",
                                                      "password": "User-pass#007"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Customer 2",
                                            summary = "Customer-user 2 (role ROLE_CUSTOMER)",
                                            value = """
                                                    {
                                                      "email": "customer_2@cr.de",
                                                      "password": "User-pass#007"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Customer 3",
                                            summary = "Customer-user 3 (role ROLE_CUSTOMER)",
                                            value = """
                                                    {
                                                      "email": "customer_3@cr.de",
                                                      "password": "User-pass#007"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Admin",
                                            summary = "Customer-admin (role ROLE_ADMIN)",
                                            value = """
                                                    {
                                                      "email": "admin_1@car-rent.de",
                                                      "password": "Admin-Pass#007"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @Valid
            LoginRequestDto loginDto);

    TokenResponseDto getNewAccessToken(
            @RequestBody RefreshRequestDto refreshRequestDto);

    @GetMapping("/confirm/{code}")
    ResponseEntity<String> confirmRegistration(@PathVariable String code);
}
