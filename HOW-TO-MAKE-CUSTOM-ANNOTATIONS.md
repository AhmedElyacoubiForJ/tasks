Wir bauen **ValidTaskTitle** so, dass es:

✅ exakt zu meiner bestehenden TaskList‑Architektur passt  
✅ modular ist  
✅ wiederverwendbar für **Create** und **Update**  
✅ Swagger‑fähig ist (über eigenen Customizer)  
✅ DDD‑freundlich bleibt  
✅ und sauber dokumentiert ist

Wir machen es **identisch** wie `ValidTaskListTitle`, nur eben für Tasks.

---

# ✅ **1. ValidTaskTitle – Annotation (finale Version)**

Diese Annotation kombiniert:

- `@NotBlank`
- `@Size(min, max)`
- eigene Fehlermeldungen
- keine eigenen Validatoren (wir nutzen die Standard‑Validatoren)

```java
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
```

✅ exakt wie `ValidTaskListTitle`  
✅ sauber, modular, wiederverwendbar  
✅ Create & Update können dieselbe Annotation nutzen

---

# ✅ **2. ValidTaskTitleCustomizer – Swagger‑Customizer**

Dieser Customizer sorgt dafür, dass Swagger:

- minLength
- maxLength
- required
- nullable=false

korrekt anzeigt.

```java
package edu.yacoubi.tasks.config;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;
import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskTitle;

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
```

✅ exakt wie dein TaskList‑Customizer  
✅ modular  
✅ sauber  
✅ keine Vermischung von Regeln  
✅ funktioniert für Create & Update

---

# ✅ **3. Integration in UpdateTaskDto**

```java
public record UpdateTaskDto(

        @Schema(description = "Titel der Aufgabe", example = "Swagger konfigurieren")
        @ValidTaskTitle
        String title,

        ...
) {}
```

✅ fertig  
✅ sauber  
✅ konsistent

---

# ✅ **4. Integration in CreateTaskDto (optional, aber empfohlen)**

Damit Create & Update dieselben Regeln nutzen:

```java
public record CreateTaskDto(

        @Schema(description = "Titel der Aufgabe", example = "Swagger konfigurieren")
        @ValidTaskTitle
        String title,

        ...
) {}
```

✅ eine Quelle der Wahrheit  
✅ keine doppelten Regeln  
✅ perfekte Konsistenz

---

# ✅ **5. Warum diese Lösung perfekt ist**

- **Modular**: Jede Regel hat ihre eigene Annotation + Customizer
- **Wiederverwendbar**: Create & Update nutzen dieselben Regeln
- **Swagger‑kompatibel**: Regeln werden sichtbar
- **DDD‑freundlich**: Validierung bleibt im API‑Layer
- **Erweiterbar**: Du kannst später `@ValidTaskStatusTransition` etc. hinzufügen
- **Konsistent**: exakt wie deine TaskList‑Validierung
