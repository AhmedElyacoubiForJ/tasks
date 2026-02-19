package edu.yacoubi.tasks.controllers.api.contract;

import edu.yacoubi.tasks.controllers.api.APIResponse;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseListTaskSummaryDto;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseTaskSummaryDto;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseVoid;
import edu.yacoubi.tasks.controllers.api.annotations.DomainErrorResponses;
import edu.yacoubi.tasks.domain.dto.request.task.CreateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.FullUpdateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.PatchTaskDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Tasks in TaskLists",
        description = "Endpunkte für Tasks innerhalb einer TaskList"
)
public interface ITaskListsTasksApi extends IApiPrefix {

    @Operation(
            summary = "Alle Tasks einer TaskList abrufen",
            description = "Gibt alle Tasks zurück, die zu einer bestimmten TaskList gehören."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste aller Tasks",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponseListTaskSummaryDto.class)
            )
    )
    @GetMapping(value = "/tasklists/{taskListId}/tasks", produces = "application/json")
    ResponseEntity<APIResponseListTaskSummaryDto> getTasksByListId(
            @Parameter(description = "UUID der TaskList")
            @PathVariable("taskListId") UUID taskListId
    );

    @Operation(
            summary = "Neuen Task in einer TaskList erstellen",
            description = "Fügt einen neuen Task zu einer bestehenden TaskList hinzu."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Task erfolgreich erstellt",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponseTaskSummaryDto.class)
            )
    )
    @PostMapping(value = "/tasklists/{taskListId}/tasks", consumes = "application/json", produces = "application/json")
    ResponseEntity<APIResponseTaskSummaryDto> createTaskInList(
            @PathVariable("taskListId") UUID taskListId,
            @Valid @RequestBody CreateTaskDto dto
    );

    @Operation(
            summary = "Task innerhalb einer TaskList aktualisieren",
            description = "Aktualisiert einen bestehenden Task innerhalb einer TaskList."
    )
    @DomainErrorResponses
    @ApiResponse(
            responseCode = "200",
            description = "Task erfolgreich aktualisiert",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponseTaskSummaryDto.class)
            )
    )
    @PutMapping(value = "/tasklists/{taskListId}/tasks/{taskId}", consumes = "application/json", produces = "application/json")
    ResponseEntity<APIResponseTaskSummaryDto> updateTaskInList(
            @PathVariable("taskListId") UUID taskListId,
            @PathVariable("taskId") UUID taskId,
            @Valid @RequestBody FullUpdateTaskDto dto
    );

    @Operation(
            summary = "Task innerhalb einer TaskList teilweise aktualisieren",
            description = "Aktualisiert einen bestehenden Task innerhalb einer TaskList partiell (PATCH)."
    )
    @DomainErrorResponses
    @ApiResponse(
            responseCode = "200",
            description = "Task erfolgreich aktualisiert (PATCH)",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponseTaskSummaryDto.class)
            )
    )
    @PatchMapping(value = "/tasklists/{taskListId}/tasks/{taskId}", consumes = "application/json", produces = "application/json")
    ResponseEntity<APIResponseTaskSummaryDto> patchTaskInList(
            @PathVariable("taskListId") UUID taskListId,
            @PathVariable("taskId") UUID taskId,
            @Valid @RequestBody PatchTaskDto dto
    );

    @Operation(
            summary = "Task innerhalb einer TaskList löschen",
            description = "Löscht einen Task aus einer bestimmten TaskList."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Task erfolgreich gelöscht",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponseVoid.class)
            )
    )
    @DeleteMapping(value = "/tasklists/{taskListId}/tasks/{taskId}", produces = "application/json")
    ResponseEntity<APIResponseVoid> deleteTaskInList(
            @PathVariable("taskListId") UUID taskListId,
            @PathVariable("taskId") UUID taskId
    );
}
