package edu.yacoubi.tasks.domain.validation.annotations;

import edu.yacoubi.tasks.domain.validation.validators.ValidTaskStatusValidator;
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
