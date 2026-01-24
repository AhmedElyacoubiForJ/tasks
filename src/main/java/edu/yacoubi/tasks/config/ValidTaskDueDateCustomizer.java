package edu.yacoubi.tasks.config;

import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskDueDate;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;

@Component
public class ValidTaskDueDateCustomizer implements PropertyCustomizer {

    @Override
    public Schema customize(Schema property, AnnotatedType type) {

        if (type.getCtxAnnotations() == null) {
            return property;
        }

        for (var annotation : type.getCtxAnnotations()) {

            if (annotation.annotationType().equals(ValidTaskDueDate.class)) {

                property.setFormat("date-time");
                property.setDescription("Muss in der Gegenwart oder Zukunft liegen");
                property.setNullable(true); // dueDate ist optional
            }
        }

        return property;
    }
}
