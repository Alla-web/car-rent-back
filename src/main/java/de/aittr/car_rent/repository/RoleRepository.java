package de.aittr.car_rent.repository;

import de.aittr.car_rent.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

   //методы, которых нет в спике CRUD-операций JpaRepository
   Optional<Role> findByTitle(String title);

}
