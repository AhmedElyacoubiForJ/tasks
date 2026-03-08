package edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.validation.validators;

import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.validation.annotations.ValidTaskStatus;
import edu.yacoubi.tasks.domain.model.enums.TaskStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidTaskStatusValidator implements ConstraintValidator<ValidTaskStatus, TaskStatus> {

    @Override
    public boolean isValid(TaskStatus value, ConstraintValidatorContext context) {
        if (value == null) {
            return false; // Status darf nicht null sein
        }

        // Optional: zusätzliche Regeln, falls nötig
        // Beispiel: ARCHIVED darf nicht direkt gesetzt werden
        // if (value == TaskStatus.ARCHIVED) return false;

        return true;
    }
}
