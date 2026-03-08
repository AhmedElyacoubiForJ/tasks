package edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.task;

import edu.yacoubi.tasks.domain.model.enums.TaskPriority;
import edu.yacoubi.tasks.domain.model.enums.TaskStatus;
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

