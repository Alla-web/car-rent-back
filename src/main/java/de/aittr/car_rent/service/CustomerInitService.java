package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.dto.CustomerRegisterDto;
import de.aittr.car_rent.domain.dto.CustomerResponseDto;
import de.aittr.car_rent.domain.entity.Role;
import de.aittr.car_rent.repository.CustomerRepository;
import de.aittr.car_rent.security.service.AuthService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для начальной инициализации тестовых пользователей (клиентов и администраторов)
 * в среде разработки (профиль "dev").
 * <p>
 * Данный сервис создает набор тестовых пользователей при старте приложения, если они отсутствуют в базе данных.
 * Для клиентов используется роль, определенная в {@code CustomerRoleService.initCustomerRole},
 * а для администраторов – роль "ROLE_ADMIN", полученная через {@link de.aittr.car_rent.service.CustomerRoleService#saveOrGet(String)}.
 * </p>
 * <p>
 * Тестовые данные пользователей задаются через статические поля:
 * <ul>
 *   <li>{@code customerPassword} и {@code adminPassword} - пароли для клиентов и администраторов соответственно;</li>
 *   <li>{@code usersListCustomer} - список регистрационных данных для клиентов;</li>
 *   <li>{@code usersListAdmin} - список регистрационных данных для администраторов.</li>
 * </ul>
 * </p>
 */
@Profile("dev")
@Service
@RequiredArgsConstructor
@Slf4j
public class  CustomerInitService {

    private static final String customerPassword = "user-pass#007";
    private static final String adminPassword = "admin-Pass#007";

    private static final List<CustomerRegisterDto> usersListCustomer = List.of(
            new CustomerRegisterDto("Mary", "Smith", "customer_1@car-rent.de", customerPassword),
            new CustomerRegisterDto("Jane", "Brothers", "customer_2@cr.de", customerPassword),
            new CustomerRegisterDto("Bob", "Builder", "customer_3@cr.de", customerPassword)
    );

    private static final List<CustomerRegisterDto> usersListAdmin = List.of(
            new CustomerRegisterDto("John", "Doe", "admin_1@car-rent.de", adminPassword)
    );

    private final AuthService authService;
    private final CustomerRoleServiceImpl customerRoleService;
    private final CustomerRepository customerRepository;

    /**
     * Метод инициализации, который запускается после создания бина.
     * <p>
     * Создает тестовых клиентов и администраторов, используя заранее заданные списки регистрационных данных.
     * Для каждого пользователя проверяется, существует ли уже клиент с указанным email.
     * Если пользователь не существует, то он создается через {@link AuthService#register(CustomerRegisterDto, Role)}.
     * </p>
     */
    @PostConstruct
    public void init() {
        final Role customerRole = CustomerRoleServiceImpl.initCustomerRole;
        usersListCustomer.forEach(c -> createCustomer(c, customerRole));

        final Role adminRole = customerRoleService.saveOrGet("ROLE_ADMIN");
        usersListAdmin.forEach(c -> createCustomer(c, adminRole));
    }

    /**
     * Создает нового клиента с заданными данными регистрации и ролью.
     * <p>
     * Если клиент с указанным email уже существует, то выводится предупреждающее сообщение в лог.
     * В противном случае происходит регистрация клиента и выводится информационное сообщение.
     * </p>
     *
     * @param registerDto  объект с данными для регистрации клиента
     * @param customerRole роль, которая будет назначена клиенту
     */
    private void createCustomer(final CustomerRegisterDto registerDto, final Role customerRole) {
        if (customerRepository.existsByEmailIgnoreCase(registerDto.email())) {
            log.warn("Customer with email {} already exists", registerDto.email());
        } else {
            CustomerResponseDto register = authService.register(registerDto, customerRole);
            log.info("Customer {} was created", register.email());
        }
    }
}
