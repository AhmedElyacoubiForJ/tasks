package edu.yacoubi.tasks.controllers.api;

import edu.yacoubi.tasks.domain.dto.response.task.TaskDto;
import edu.yacoubi.tasks.domain.responses.APIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Subresource-Endpunkte für Tasks innerhalb einer TaskList.
 * Erbt zentrale Metadaten aus IBaseTaskListsApi.
 */
public interface ITaskListsTasksApi extends IBaseTaskListsApi {

    @Operation(
            summary = "Alle Tasks einer TaskList abrufen",
            description = "Gibt alle Tasks zurück, die zu einer bestimmten TaskList gehören."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste aller Tasks",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "TaskList nicht gefunden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)))
    })
    @GetMapping(value = "/tasklists/{id}/tasks", produces = "application/json")
    ResponseEntity<APIResponse<List<TaskDto>>> getTasksByListId(
            @Parameter(description = "UUID der TaskList", required = true)
            @PathVariable UUID id
    );

    @Operation(
            summary = "Neuen Task in einer TaskList erstellen",
            description = "Fügt einen neuen Task zu einer bestehenden TaskList hinzu."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task erfolgreich erstellt",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "TaskList nicht gefunden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)))
    })
    @PostMapping(value = "/tasklists/{id}/tasks", consumes = "application/json", produces = "application/json")
    ResponseEntity<APIResponse<TaskDto>> createTaskInList(
            @Parameter(description = "UUID der TaskList", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Daten für neuen Task")
            @Valid @RequestBody TaskDto dto
    );

    @Operation(
            summary = "Task innerhalb einer TaskList aktualisieren",
            description = "Aktualisiert einen bestehenden Task innerhalb einer TaskList."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task erfolgreich aktualisiert",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "Task oder TaskList nicht gefunden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)))
    })
    @PutMapping(value = "/tasklists/{id}/tasks/{taskId}", consumes = "application/json", produces = "application/json")
    ResponseEntity<APIResponse<TaskDto>> updateTaskInList(
            @Parameter(description = "UUID der TaskList", required = true)
            @PathVariable UUID id,
            @Parameter(description = "UUID des Tasks", required = true)
            @PathVariable UUID taskId,
            @Parameter(description = "Daten für Task-Update")
            @Valid @RequestBody TaskDto dto
    );

    @Operation(
            summary = "Task innerhalb einer TaskList löschen",
            description = "Löscht einen Task aus einer bestimmten TaskList."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task erfolgreich gelöscht",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "Task oder TaskList nicht gefunden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)))
    })
    @DeleteMapping(value = "/tasklists/{id}/tasks/{taskId}", produces = "application/json")
    ResponseEntity<APIResponse<Void>> deleteTaskInList(
            @Parameter(description = "UUID der TaskList", required = true)
            @PathVariable UUID id,
            @Parameter(description = "UUID des Tasks", required = true)
            @PathVariable UUID taskId
    );
}
