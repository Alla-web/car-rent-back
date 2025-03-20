package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.entity.Role;
import de.aittr.car_rent.exception_handling.exceptions.RestApiException;
import de.aittr.car_rent.repository.RoleRepository;
import de.aittr.car_rent.service.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleUser() {
        return roleRepository.findByTitle("CUSTOMER")
                .orElseThrow(() -> new RestApiException("Role CUSTOMER not found"));
    }

    @Override
    public Role getRoleAdmin() {
        return roleRepository.findByTitle("ADMIN")
                .orElseThrow(() -> new RestApiException("Role ADMIN not found"));
    }
}
