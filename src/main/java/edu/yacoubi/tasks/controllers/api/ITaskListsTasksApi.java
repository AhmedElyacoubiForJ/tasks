package edu.yacoubi.tasks.controllers.api;

import edu.yacoubi.tasks.domain.dto.request.task.CreateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.FullUpdateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.PatchTaskDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Subresource-Endpunkte für Tasks innerhalb einer TaskList.
 * Erbt zentrale Metadaten aus IBaseTaskListsApi.
 */
@Tag(
        name = "Tasks in TaskLists",
        description = "Endpunkte für Tasks innerhalb einer TaskList"
)
public interface ITaskListsTasksApi extends IApiPrefix {

    @Operation(
            summary = "Alle Tasks einer TaskList abrufen",
            description = "Gibt alle Tasks zurück, die zu einer bestimmten TaskList gehören."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste aller Tasks",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "TaskList nicht gefunden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            )
    })
    @GetMapping(
            value = "/tasklists/{taskListId}/tasks",
            produces = "application/json"
    )
    ResponseEntity<APIResponse<List<TaskSummaryDto>>> getTasksByListId(
            @Parameter(description = "UUID der TaskList", required = true)
            @PathVariable("taskListId") UUID taskListId
    );

    @Operation(
            summary = "Neuen Task in einer TaskList erstellen",
            description = "Fügt einen neuen Task zu einer bestehenden TaskList hinzu."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Task erfolgreich erstellt",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ungültige Eingabe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "TaskList nicht gefunden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            )
    })
    @PostMapping(
            value = "/tasklists/{taskListId}/tasks",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<APIResponse<TaskSummaryDto>> createTaskInList(
            @Parameter(description = "UUID der TaskList", required = true)
            @PathVariable("taskListId") UUID taskListId,
            @Parameter(description = "Daten für neuen Task")
            @Valid @RequestBody CreateTaskDto dto
    );

    @Operation(
            summary = "Task innerhalb einer TaskList aktualisieren",
            description = "Aktualisiert einen bestehenden Task innerhalb einer TaskList."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task erfolgreich aktualisiert",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ungültige Eingabe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task oder TaskList nicht gefunden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            )
    })
    @PutMapping(
            value = "/tasklists/{taskListId}/tasks/{taskId}",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<APIResponse<TaskSummaryDto>> updateTaskInList(
            @Parameter(description = "UUID der TaskList", required = true)
            @PathVariable("taskListId") UUID taskListId,
            @Parameter(description = "UUID des Tasks", required = true)
            @PathVariable("taskId") UUID taskId,
            @Parameter(description = "Daten für Task-Update")
            @Valid @RequestBody FullUpdateTaskDto dto
    );

    @Operation(
            summary = "Task innerhalb einer TaskList teilweise aktualisieren",
            description = "Aktualisiert einen bestehenden Task innerhalb einer TaskList partiell (PATCH)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task erfolgreich aktualisiert (PATCH)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ungültige Eingabe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task oder TaskList nicht gefunden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            )
    })
    @PatchMapping(
            value = "/tasklists/{taskListId}/tasks/{taskId}",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<APIResponse<TaskSummaryDto>> patchTaskInList(
            @Parameter(description = "UUID der TaskList", required = true)
            @PathVariable("taskListId") UUID taskListId,
            @Parameter(description = "UUID des Tasks", required = true)
            @PathVariable("taskId") UUID taskId,
            @Parameter(description = "Daten für partielle Aktualisierung des Tasks")
            @Valid @RequestBody PatchTaskDto dto
    );

    @Operation(
            summary = "Task innerhalb einer TaskList löschen",
            description = "Löscht einen Task aus einer bestimmten TaskList. "
                    + "Gibt eine APIResponse zurück, die den Erfolg bestätigt."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task erfolgreich gelöscht",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task oder TaskList nicht gefunden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            )
    })
    @DeleteMapping(
            value = "/tasklists/{taskListId}/tasks/{taskId}",
            produces = "application/json"
    )
    ResponseEntity<APIResponse<Void>> deleteTaskInList(
            @Parameter(description = "UUID der TaskList", required = true)
            @PathVariable("taskListId") UUID taskListId,
            @Parameter(description = "UUID des Tasks", required = true)
            @PathVariable("taskId") UUID taskId
    );
}
