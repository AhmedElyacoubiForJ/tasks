package edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.tasklist;

import edu.yacoubi.tasks.domain.model.enums.TaskStatus;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.validation.annotations.ValidTaskStatus;
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
