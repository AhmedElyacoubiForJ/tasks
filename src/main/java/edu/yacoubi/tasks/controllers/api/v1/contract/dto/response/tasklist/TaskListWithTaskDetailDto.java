package edu.yacoubi.tasks.controllers.api.v1.contract.dto.response.tasklist;

import edu.yacoubi.tasks.controllers.api.v1.contract.dto.response.task.TaskDetailDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Task-Liste mit vollständigen Details zu den enthaltenen Aufgaben")
public record TaskListWithTaskDetailDto(

        @Schema(description = "ID der Task-Liste", example = "640eb884-723b-408a-869d-59b38ca44b84")
        UUID id,

        @Schema(description = "Titel der Task-Liste", example = "🏁 Demo-Liste")
        String title,

        @Schema(description = "Beschreibung der Task-Liste", example = "Beispielhafte Aufgaben für Swagger & Seeding")
        String description,

        @Schema(description = "Anzahl der enthaltenen Tasks", example = "2")
        Integer count,

        @Schema(description = "Fortschritt in Prozent", example = "50.0")
        Double progress,

        @Schema(description = "Liste der vollständigen Task-Details")
        List<TaskDetailDto> tasks
) {
}
