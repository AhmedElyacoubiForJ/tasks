package edu.yacoubi.tasks.config;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;
import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskListTitle;

@Component
public class ValidTaskListTitleCustomizer implements PropertyCustomizer {

    @Override
    public Schema customize(Schema property, AnnotatedType type) {
        if (type.getCtxAnnotations() != null) {
            for (var annotation : type.getCtxAnnotations()) {
                if (annotation.annotationType().equals(ValidTaskListTitle.class)) {
                    property.setMinLength(3);
                    property.setMaxLength(100);
                    property.setNullable(false); // entspricht @NotBlank
                    // Feldname als Pflichtfeld markieren
                    if (type.getType() instanceof Class<?>) {
                        String fieldName = type.getType().getTypeName(); // liefert den Typnamen
                        // einfacher: direkt "title" setzen
                        property.addRequiredItem("title");
                    }
                }
            }
        }
        return property;
    }
}
