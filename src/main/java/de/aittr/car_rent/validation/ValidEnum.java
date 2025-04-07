package de.aittr.car_rent.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ ElementType.FIELD }) // можно также добавить METHOD, PARAMETER если нужно
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {

    String message() default "Invalid value for enum";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Enum<?>> enumClass();

}