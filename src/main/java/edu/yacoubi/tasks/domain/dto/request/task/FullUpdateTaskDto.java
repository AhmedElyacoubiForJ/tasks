package edu.yacoubi.tasks.domain.dto.request.task;

import edu.yacoubi.tasks.domain.entities.TaskPriority;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskDescription;
import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskDueDate;
import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "DTO für ein vollständiges Update einer bestehenden Aufgabe (Full Update / PUT)")
public record FullUpdateTaskDto(

        @Schema(description = "Titel der Aufgabe", example = "Swagger konfigurieren")
        @ValidTaskTitle
        @NotNull
        String title,

        @Schema(
                description = "Beschreibung der Aufgabe",
                example = "Swagger-Dokumentation für Task-Endpunkte überarbeiten")
        @ValidTaskDescription
        @NotNull
        String description,

        @Schema(
                description = "Fälligkeitsdatum (muss in der Gegenwart oder Zukunft liegen)",
                example = "2025-08-01T14:30:00")
        @ValidTaskDueDate
        @NotNull
        LocalDateTime dueDate,

        @Schema(description = "Aktueller Status der Aufgabe", example = "OPEN")
        @NotNull
        TaskStatus status,

        @Schema(description = "Aktualisierte Priorität der Aufgabe", example = "MEDIUM")
        @NotNull
        TaskPriority priority
) {}
