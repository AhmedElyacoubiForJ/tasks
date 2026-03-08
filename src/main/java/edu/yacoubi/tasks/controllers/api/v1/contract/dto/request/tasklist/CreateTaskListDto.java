package edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.tasklist;

import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.validation.annotations.ValidTaskListDescription;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.validation.annotations.ValidTaskListTitle;
import io.swagger.v3.oas.annotations.media.Schema;

// Hinweis: Wir verwenden hier bewusst @ValidTaskListTitle statt @NotBlank/@Size,
// damit die Constraints über Springdoc in Swagger sichtbar werden.
// Die Standard-Annotations sind trotzdem importiert, falls wir später
// wieder direkt mit @NotBlank/@Size arbeiten wollen oder die Custom-Validatoren erweitern.


@Schema(description = "DTO zur Erstellung einer neuen Task-Liste")
public record CreateTaskListDto(

        //@NotBlank(message = "Titel darf nicht leer sein")
        //@Size(min = 3, max = 100, message = "Titel muss zwischen 3 und 100 Zeichen lang sein")
        @ValidTaskListTitle
        @Schema(description = "Titel der Task-Liste", example = "🏁 Demo-Liste")
        String title,

        //@Size(max = 500, message = "Beschreibung darf maximal 500 Zeichen lang sein")
        @ValidTaskListDescription
        @Schema(
                description = "Beschreibung der Task-Liste",
                example = "Beispielhafte Aufgaben für Swagger & Seeding"
        )
        String description
) {
}
