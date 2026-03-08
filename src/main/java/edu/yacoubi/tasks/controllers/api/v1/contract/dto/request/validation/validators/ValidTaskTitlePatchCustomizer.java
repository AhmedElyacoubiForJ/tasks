package edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.validation.validators;

import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.validation.annotations.ValidTaskTitlePatch;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;

@Component
public class ValidTaskTitlePatchCustomizer implements PropertyCustomizer {

    @Override
    public Schema customize(Schema property, AnnotatedType type) {

        if (type.getCtxAnnotations() == null) return property;

        for (var annotation : type.getCtxAnnotations()) {
            if (annotation.annotationType().equals(ValidTaskTitlePatch.class)) {
                property.setMinLength(3);
                property.setMaxLength(200);
                property.setNullable(true); // PATCH → optional
            }
        }

        return property;
    }
}

