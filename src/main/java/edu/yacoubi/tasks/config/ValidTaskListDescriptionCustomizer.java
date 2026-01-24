package edu.yacoubi.tasks.config;

import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskListDescription;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;

@Component
public class ValidTaskListDescriptionCustomizer implements PropertyCustomizer {

    @Override
    public Schema customize(Schema property, AnnotatedType type) {
        if (type.getCtxAnnotations() != null) {
            for (var annotation : type.getCtxAnnotations()) {
                if (annotation.annotationType().equals(ValidTaskListDescription.class)) {
                    property.setMaxLength(255);
                }
            }
        }
        return property;
    }
}
