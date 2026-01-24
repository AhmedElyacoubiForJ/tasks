package edu.yacoubi.tasks.domain.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 3, max = 200, message = "Titel muss zwischen 3 und 200 Zeichen lang sein")
public @interface ValidTaskTitlePatch {

    String message() default "Ungültiger Titel für Task (PATCH)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
