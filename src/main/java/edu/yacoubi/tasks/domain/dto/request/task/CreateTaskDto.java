package edu.yacoubi.tasks.domain.dto.request.task;

import edu.yacoubi.tasks.domain.entities.TaskPriority;
import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskDescription;
import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskDueDate;
import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO zur Erstellung einer neuen Aufgabe")
public record CreateTaskDto(

        @Schema(description = "Titel der Aufgabe", example = "Swagger konfigurieren")
        @ValidTaskTitle
        String title,

        @Schema(description = "Beschreibung der Aufgabe", example = "Swagger-Dokumentation für Task-Endpunkte erstellen")
        @ValidTaskDescription
        String description,

        //@NotNull(message = "Fälligkeitsdatum darf nicht null sein")
        //@FutureOrPresent(message = "Fälligkeitsdatum muss in der Gegenwart oder Zukunft liegen")
        @ValidTaskDueDate
        @Schema(description = "Fälligkeitsdatum der Aufgabe", example = "2025-07-28T13:32:16")
        LocalDateTime dueDate,

        @NotNull(message = "Priorität darf nicht null sein")
        @Schema(description = "Priorität der Aufgabe", example = "HIGH")
        TaskPriority priority,

        @NotNull(message = "ID der zugehörigen Task-Liste darf nicht null sein")
        @Schema(description = "ID der zugehörigen Task-Liste", example = "640eb884-723b-408a-869d-59b38ca44b84")
        UUID taskListId
) {
}
