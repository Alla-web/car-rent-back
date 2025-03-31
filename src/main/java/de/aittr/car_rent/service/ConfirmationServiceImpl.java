package de.aittr.car_rent.service;

import de.aittr.car_rent.domain.entity.ConfirmationCode;
import de.aittr.car_rent.domain.entity.Customer;
import de.aittr.car_rent.exception_handling.Response;
import de.aittr.car_rent.repository.ConfirmationCodeRepository;
import de.aittr.car_rent.service.interfaces.ConfirmationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.ConstructorParameters;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {
    private final ConfirmationCodeRepository repository;


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

        if(optionalCode.isEmpty()){
            throw new RuntimeException("Invalid confirmation code");
        }

        ConfirmationCode confirmationCode = optionalCode.get();

        if (confirmationCode.getExpired().isBefore(LocalDateTime.now())){
            repository.delete(confirmationCode);
        }

        Customer customer = confirmationCode.getCustomer();
        customer.setActive(true);

        return true;
    }
}



