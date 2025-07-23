package edu.yacoubi.tasks.domain.dto.request.task;

import edu.yacoubi.tasks.domain.entities.TaskPriority;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO zur Aktualisierung einer bestehenden Aufgabe")
public record UpdateTaskDto(

        @Schema(description = "ID der Aufgabe", example = "639de273-4ad7-4367-8771-37b6f4ee030b")
        @NotNull
        UUID id,

        @Schema(description = "Titel der Aufgabe", example = "Swagger konfigurieren")
        @NotBlank
        String title,

        @Schema(description = "Beschreibung der Aufgabe", example = "Swagger-Dokumentation für Task-Endpunkte überarbeiten")
        String description,

        @Schema(description = "Fälligkeitsdatum (muss in der Gegenwart oder Zukunft liegen)", example = "2025-08-01T14:30:00")
        @FutureOrPresent
        LocalDateTime dueDate,

        @Schema(description = "Aktueller Status der Aufgabe", example = "OPEN")
        @NotNull
        TaskStatus status,

        @Schema(description = "Aktualisierte Priorität der Aufgabe", example = "MEDIUM")
        @NotNull
        TaskPriority priority
) {
}
