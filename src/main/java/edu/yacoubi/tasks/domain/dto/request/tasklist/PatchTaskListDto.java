package edu.yacoubi.tasks.domain.dto.request.tasklist;

import edu.yacoubi.tasks.domain.entities.TaskListStatus;
import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskListDescription;
import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskListTitle;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO für partielle Aktualisierungen einer Task-Liste (PATCH)")
public record PatchTaskListDto(

        @ValidTaskListTitle
        @Schema(
                description = "Neuer Titel der Task-Liste (optional)",
                example = "📝 Überarbeitete Planung"
        )
        String title,

        @ValidTaskListDescription
        @Schema(
                description = "Neue Beschreibung der Task-Liste (optional)",
                example = "Aktualisierte Beschreibung für Sprint 3"
        )
        String description,

        @Schema(
                description = "Neuer Status der Task-Liste (optional)",
                example = "ACTIVE"
        )
        TaskListStatus status
) {
}
