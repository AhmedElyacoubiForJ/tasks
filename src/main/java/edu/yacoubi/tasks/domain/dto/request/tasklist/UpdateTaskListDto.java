package edu.yacoubi.tasks.domain.dto.request.tasklist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "DTO zur Aktualisierung einer bestehenden Task-Liste")
public record UpdateTaskListDto(

        @Schema(description = "ID der Task-Liste", example = "640eb884-723b-408a-869d-59b38ca44b84")
        @NotNull
        UUID id,

        @Schema(description = "Neuer Titel der Task-Liste", example = "🚀 Geplante Features")
        @NotBlank
        String title,

        @Schema(description = "Aktualisierte Beschreibung der Task-Liste", example = "Aufgaben für Sprint 2 inkl. Backend-Komponenten")
        String description
) {
}
