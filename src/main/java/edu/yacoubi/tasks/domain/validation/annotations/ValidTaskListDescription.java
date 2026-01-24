package edu.yacoubi.tasks.domain.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {}) // nur bestehender Validator @Size
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Size(max = 255, message = "Beschreibung darf maximal 255 Zeichen lang sein")
public @interface ValidTaskListDescription {

    String message() default "Ungültige Beschreibung für TaskList";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
