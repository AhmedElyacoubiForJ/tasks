package edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.validation.annotations;

import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.validation.validators.ValidTaskStatusValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidTaskStatusValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTaskStatus {

    String message() default "Ungültiger Task-Status";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
