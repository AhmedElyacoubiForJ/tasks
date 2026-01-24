package edu.yacoubi.tasks.domain.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {}) // wir nutzen nur @Size
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(max = 2000, message = "Beschreibung darf maximal 2000 Zeichen lang sein")
public @interface ValidTaskDescription {

    String message() default "Ungültige Beschreibung für Task";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
