package edu.yacoubi.tasks.domain.dto.request.tasklist;

import edu.yacoubi.tasks.domain.entities.TaskStatus;
import edu.yacoubi.tasks.domain.validation.annotations.ValidTaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO für das Ändern des Status eines Tasks innerhalb einer TaskList")
public record ChangeTaskStatusRequest(

        @Schema(
                description = "Neuer Status des Tasks",
                example = "COMPLETED"
        )
        @ValidTaskStatus
        TaskStatus status

) {}
