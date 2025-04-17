package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.entity.Role;
import de.aittr.car_rent.repository.RoleRepository;
import de.aittr.car_rent.service.interfaces.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleUser() {
        return roleRepository.findByTitle("ROLE_CUSTOMER").orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Override
    public Role getRoleAdmin() {
        return roleRepository.findByTitle("ROLE_ADMIN").orElseThrow(() -> new RuntimeException("Role not found"));
    }
}
