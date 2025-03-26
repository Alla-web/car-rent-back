package de.aittr.car_rent.repository;

import de.aittr.car_rent.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c where c.isActive = true")
    List<Customer> findAllByActiveTrue();

    @Query("select c from Customer c where upper(c.email) = upper(?1)")
    Optional<Customer> findByEmail(String email);

    @Query("select (count(c) > 0) from Customer c where upper(c.email) = upper(?1)")
    boolean existsByEmailIgnoreCase(String email);
}
