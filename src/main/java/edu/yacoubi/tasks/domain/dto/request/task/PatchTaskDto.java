package edu.yacoubi.tasks.domain.dto.request.task;

import edu.yacoubi.tasks.domain.entities.TaskPriority;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskDescription;
import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskDueDate;
import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskTitlePatch;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "DTO für partielle Aktualisierung einer bestehenden Aufgabe (PATCH)")
public record PatchTaskDto(

        @Schema(description = "Neuer Titel der Aufgabe (optional)",
                example = "Swagger konfigurieren")
        @ValidTaskTitlePatch
        String title,

        @Schema(description = "Neue Beschreibung der Aufgabe (optional)",
                example = "Swagger-Dokumentation für Task-Endpunkte überarbeiten")
        @ValidTaskDescription
        String description,

        @Schema(description = "Neues Fälligkeitsdatum (optional, muss in der Gegenwart oder Zukunft liegen)",
                example = "2025-08-01T14:30:00")
        @ValidTaskDueDate
        LocalDateTime dueDate,

        @Schema(description = "Neuer Status der Aufgabe (optional)",
                example = "IN_PROGRESS")
        TaskStatus status,

        @Schema(description = "Neue Priorität der Aufgabe (optional)",
                example = "HIGH")
        TaskPriority priority
) {}
