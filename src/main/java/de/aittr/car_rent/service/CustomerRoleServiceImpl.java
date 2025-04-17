package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.entity.Role;
import de.aittr.car_rent.repository.RoleRepository;
import de.aittr.car_rent.service.interfaces.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomerRoleServiceImpl implements RoleService.CustomerRoleService {

    /**
     * Название роли по умолчанию для клиентов.
     */
    public static final String DEFAULT_ROLE_CUSTOMER = "ROLE_CUSTOMER";

    /**
     * Роль клиента, инициализированная при старте приложения.
     */
    public static Role initCustomerRole;

    /**
     * Репозиторий для работы с сущностями Role.
     */
    public final RoleRepository roleRepository;

    /**
     * Метод инициализации ролей, который вызывается после создания бина.
     * <p>
     * Устанавливает роль по умолчанию для клиентов, используя метод {@link #saveOrGet(String)}.
     * </p>
     */
    @PostConstruct
    public void initRoles() {
        initCustomerRole = saveOrGet(DEFAULT_ROLE_CUSTOMER);
    }

    @Override
    public Role saveOrGet(String roleName) {
        return findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }


    @Override
    public Optional<Role> findByName(String roleName) {
        return roleRepository.findByTitle(roleName);
    }
}
