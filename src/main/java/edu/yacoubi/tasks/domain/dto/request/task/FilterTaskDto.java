package edu.yacoubi.tasks.domain.dto.request.task;

import edu.yacoubi.tasks.domain.entities.TaskPriority;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filteroptionen für Aufgaben")
public record FilterTaskDto(
        @Schema(description = "Suchbegriff im Titel oder Beschreibung", example = "Swagger")
        String query,
        @Schema(description = "Status der Aufgabe zum Filtern", example = "OPEN")
        TaskStatus status,
        @Schema(description = "Priorität der Aufgabe zum Filtern", example = "HIGH")
        TaskPriority priority
) {
}

