package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.entity.ConfirmationCode;
import de.aittr.car_rent.domain.entity.Customer;
import de.aittr.car_rent.exception_handling.exceptions.RestApiException;
import de.aittr.car_rent.repository.ConfirmationCodeRepository;
import de.aittr.car_rent.service.interfaces.ConfirmationService;
import de.aittr.car_rent.service.interfaces.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {
    private final ConfirmationCodeRepository repository;
    private final CustomerService customerService;


    @Override
    public String generateConfirmationCode(Customer customer) {
        String code = UUID.randomUUID().toString();
        LocalDateTime expired = LocalDateTime.now().plusMinutes(5);
        ConfirmationCode codeEntity = new ConfirmationCode(customer, expired, code);
        repository.save(codeEntity);
        return code;
    }

    @Transactional
    @Override
    public boolean confirmRegistration(String code) {
        Optional<ConfirmationCode> optionalCode = repository.findByCode(code);

        if (optionalCode.isEmpty()) {
            throw new RestApiException("Invalid confirmation code");
        }

        ConfirmationCode confirmationCode = optionalCode.get();
        if (confirmationCode.getExpired().isBefore(LocalDateTime.now())) {
            throw new RestApiException("Invalid confirmation code");
        }

        Customer customer = confirmationCode.getCustomer();
        if (Objects.isNull(customer)) {
            throw new RestApiException("Invalid confirmation code");
        }

        customer.setActive(true);
        customerService.save(customer);
        repository.delete(confirmationCode);
        return true;
    }
}



