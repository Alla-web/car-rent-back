package de.aittr.car_rent.security.controller;

import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.security.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Интерфейс для AuthTestController, предоставляющий описание API с использованием Swagger.
 * <p>
 * Данный интерфейс описывает эндпоинты для тестирования аутентификации:
 * <ul>
 *   <li>Получение данных администратора (требуется роль ROLE_ADMIN)</li>
 *   <li>Получение данных клиента (требуется роль ROLE_CUSTOMER)</li>
 *   <li>Получение данных для любого аутентифицированного пользователя</li>
 *   <li>Не защищённый эндпоинт</li>
 * </ul>
 * </p>
 */
@Tag(name = "Authentication test controller")
public interface AuthTestController {

    /**
     * Возвращает данные клиента, авторизованного как администратор.
     *
     * @param authUserEmail Email аутентифицированного пользователя (параметр скрыт в документации)
     * @return объект {@link CustomerResponseDto} с данными администратора
     */
    @Operation(
            summary = "Получение данных администратора",
            description = "Возвращает данные клиента, авторизованного как администратор. Требуется роль ROLE_ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные администратора успешно получены",
                    content = @Content(schema = @Schema(implementation = CustomerResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "message": "User not authenticated"
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён для данного пользователя",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "message": "Access denied for user with email <user@example.com> and role [ROLE_ADMIN]"
                                    }
                                    """)
                    ))

    })
    CustomerResponseDto withAdminUser(String authUserEmail);

    /**
     * Возвращает данные клиента, авторизованного как клиент.
     *
     * @param authUserEmail Email аутентифицированного пользователя (параметр скрыт в документации)
     * @return объект {@link CustomerResponseDto} с данными клиента
     */
    @Operation(
            summary = "Получение данных клиента",
            description = "Возвращает данные клиента, авторизованного как клиент. Требуется роль ROLE_CUSTOMER."
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные клиента успешно получены",
                    content = @Content(schema = @Schema(implementation = CustomerResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "message": "User not authenticated"
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён для данного пользователя",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "message": "Access denied for user with email <user@example.com> and role [ROLE_ADMIN]"
                                    }
                                    """)
                    ))
    })
    CustomerResponseDto withCustomerUser(String authUserEmail);

    /**
     * Возвращает данные для любого аутентифицированного пользователя.
     *
     * @param authUserEmail Email аутентифицированного пользователя (параметр скрыт в документации)
     * @return объект {@link CustomerResponseDto} с данными пользователя
     */
    @Operation(
            summary = "Получение данных для любого аутентифицированного пользователя",
            description = "Возвращает данные клиента для любого аутентифицированного пользователя."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные аутентифицированного пользователя успешно получены",
                    content = @Content(schema = @Schema(implementation = CustomerResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "message": "User not authenticated"
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён для данного пользователя",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "message": "Access denied for user with email <user@example.com> and role [ROLE_ADMIN]"
                                    }
                                    """)
                    ))
    })
    CustomerResponseDto withAnyAuthentication(String authUserEmail);

    /**
     * Возвращает сообщение о том, что эндпоинт не защищён.
     *
     * @return строка с сообщением "Endpoint not secured"
     */
    @Operation(
            summary = "Не защищённый эндпоинт",
            description = "Возвращает сообщение, что данный эндпоинт не защищён."
    )
    @ApiResponse(responseCode = "200", description = "Сообщение успешно получено",
            content = @Content(schema = @Schema(implementation = String.class)))
    String notSecured();
}
