package edu.yacoubi.tasks.config;

import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskTitle;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;

@Component
public class ValidTaskTitleCustomizer implements PropertyCustomizer {

    @Override
    public Schema customize(Schema property, AnnotatedType type) {

        if (type.getCtxAnnotations() == null) {
            return property;
        }

        for (var annotation : type.getCtxAnnotations()) {

            if (annotation.annotationType().equals(ValidTaskTitle.class)) {

                property.setMinLength(3);
                property.setMaxLength(200);
                property.setNullable(false);

                // Feldname als required markieren
                property.addRequiredItem("title");
            }
        }

        return property;
    }
}
