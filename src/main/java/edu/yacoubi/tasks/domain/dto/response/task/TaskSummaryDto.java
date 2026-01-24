package edu.yacoubi.tasks.domain.dto.response.task;

import edu.yacoubi.tasks.domain.entities.TaskPriority;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Zusammenfassende Informationen zu einer Aufgabe")
public record TaskSummaryDto(

        @Schema(description = "ID der Aufgabe", example = "639de273-4ad7-4367-8771-37b6f4ee030b")
        UUID id,

        @Schema(description = "Titel der Aufgabe", example = "Swagger konfigurieren")
        String title,

        @Schema(description = "Beschreibung der Aufgabe", example = "Swagger-Dokumentation für Task-Endpunkte erstellen")
        String description,

        @Schema(description = "Fälligkeitsdatum der Aufgabe", example = "2025-07-28T13:32:16.885754")
        LocalDateTime dueDate,

        @Schema(description = "Priorität der Aufgabe", example = "HIGH")
        TaskPriority priority,

        @Schema(description = "Status der Aufgabe", example = "OPEN")
        TaskStatus status,

        @Schema(description = "ID der zugehörigen Task-Liste", example = "640eb884-723b-408a-869d-59b38ca44b84")
        UUID taskListId
) {
}
