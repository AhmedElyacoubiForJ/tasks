package edu.yacoubi.tasks.domain.dto.request.task;

import edu.yacoubi.tasks.domain.entities.TaskPriority;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO zur Erstellung einer neuen Aufgabe")
public record CreateTaskDto(

        @Schema(description = "Titel der Aufgabe", example = "Swagger konfigurieren")
        @NotBlank
        String title,

        @Schema(description = "Beschreibung der Aufgabe", example = "Swagger-Dokumentation für Task-Endpunkte erstellen")
        String description,

        @Schema(description = "Fälligkeitsdatum (muss in der Gegenwart oder Zukunft liegen)", example = "2025-07-28T13:32:16")
        @FutureOrPresent
        LocalDateTime dueDate,

        @Schema(description = "Status der Aufgabe", example = "OPEN")
        @NotNull
        TaskStatus status,

        @Schema(description = "Priorität der Aufgabe", example = "HIGH")
        @NotNull
        TaskPriority priority,

        @Schema(description = "ID der zugehörigen Task-Liste", example = "640eb884-723b-408a-869d-59b38ca44b84")
        @NotNull
        UUID taskListId
) {
}
