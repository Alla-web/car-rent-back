package de.aittr.car_rent.service.interfaces;

import de.aittr.car_rent.domain.entity.Customer;

public interface ConfirmationService {

    String generateConfirmationCode(Customer customer);

    boolean confirmRegistration(String code);
}
