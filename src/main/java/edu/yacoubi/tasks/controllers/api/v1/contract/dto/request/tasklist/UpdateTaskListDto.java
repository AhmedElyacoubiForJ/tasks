package edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.tasklist;

import edu.yacoubi.tasks.domain.model.enums.TaskListStatus;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.validation.annotations.ValidTaskListDescription;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.validation.annotations.ValidTaskListTitle;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO zur Aktualisierung einer bestehenden Task-Liste")
public record UpdateTaskListDto(

        @ValidTaskListTitle
        @Schema(
                description = "Neuer Titel der Task-Liste",
                example = "🚀 Geplante Features"
        )
        String title,

        @ValidTaskListDescription
        @Schema(
                description = "Aktualisierte Beschreibung der Task-Liste",
                example = "Aufgaben für Sprint 2 inkl. Backend-Komponenten"
        )
        String description,

        @Schema( description = "Neuer Status der Task-Liste", example = "ARCHIVED" )
        TaskListStatus status
) {
}
