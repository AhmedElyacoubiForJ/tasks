package edu.yacoubi.tasks.domain.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {}) // wir nutzen nur bestehende Validatoren
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Titel darf nicht leer sein")
@Size(min = 3, max = 200, message = "Titel muss zwischen 3 und 200 Zeichen lang sein")
public @interface ValidTaskTitle {

    String message() default "Ungültiger Titel für Task";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
