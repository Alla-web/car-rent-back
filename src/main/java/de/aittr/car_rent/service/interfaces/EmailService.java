package de.aittr.car_rent.service.interfaces;

import de.aittr.car_rent.domain.entity.Customer;

public interface EmailService {

    void sendConfirmationEmail(Customer customer);
}
