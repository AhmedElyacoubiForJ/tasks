package edu.yacoubi.tasks.domain.dto.request.tasklist;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO zur Erstellung einer neuen Task-Liste")
public record CreateTaskListDto(

        @Schema(description = "Titel der Task-Liste", example = "🏁 Demo-Liste")
        @NotBlank
        String title,

        @Schema(description = "Beschreibung der Task-Liste", example = "Beispielhafte Aufgaben für Swagger & Seeding")
        String description
) {
}
