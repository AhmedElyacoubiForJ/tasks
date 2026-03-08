package edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.FutureOrPresent;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {}) // wir nutzen nur bestehende Validatoren
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@FutureOrPresent(message = "Fälligkeitsdatum muss in der Gegenwart oder Zukunft liegen")
public @interface ValidTaskDueDate {

    String message() default "Ungültiges Fälligkeitsdatum für Task";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
