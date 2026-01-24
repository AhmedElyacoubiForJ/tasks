package edu.yacoubi.tasks.config;

import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskDescription;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;

@Component
public class ValidTaskDescriptionCustomizer implements PropertyCustomizer {

    @Override
    public Schema customize(Schema property, AnnotatedType type) {

        if (type.getCtxAnnotations() == null) {
            return property;
        }

        for (var annotation : type.getCtxAnnotations()) {

            if (annotation.annotationType().equals(ValidTaskDescription.class)) {
                property.setMaxLength(2000);
                property.setNullable(true); // Beschreibung ist optional
            }
        }

        return property;
    }
}
