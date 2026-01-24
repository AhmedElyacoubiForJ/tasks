package edu.yacoubi.tasks.controllers.api;

import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.UUID;

/**
 * Szenario-Endpunkte für TaskLists (z. B. Archivierung, Status-Prüfung).
 * Erbt zentrale Metadaten aus IBaseTaskListsApi.
 */
public interface ITaskListsScenarioApi extends IBaseTaskListsApi {

    @Operation(
            summary = "Aktive TaskLists abrufen",
            description = "Gibt eine Liste aller aktiven TaskLists zurück. Leere Liste, wenn keine vorhanden."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste aller aktiven TaskLists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)))
    })
    @GetMapping(value = "/tasklists/active", produces = "application/json")
    ResponseEntity<APIResponse<List<TaskListDto>>> getActiveTaskLists();

    @Operation(
            summary = "Archivierte TaskLists abrufen",
            description = "Gibt eine Liste aller archivierten TaskLists zurück. Leere Liste, wenn keine vorhanden."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste aller archivierten TaskLists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)))
    })
    @GetMapping(value = "/tasklists/archived", produces = "application/json")
    ResponseEntity<APIResponse<List<TaskListDto>>> getArchivedTaskLists();

    @Operation(
            summary = "Prüfen ob TaskList archivierbar ist",
            description = "Überprüft, ob alle Tasks abgeschlossen sind und die TaskList archiviert werden kann."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Archivierbarkeit erfolgreich geprüft",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "TaskList nicht gefunden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)))
    })
    @GetMapping(value = "/tasklists/{id}/archivable", produces = "application/json")
    ResponseEntity<APIResponse<Boolean>> isArchivable(@Parameter(description = "UUID der TaskList") UUID id);

    @Operation(
            summary = "TaskList archivieren",
            description = "Archiviert eine TaskList, wenn alle enthaltenen Tasks abgeschlossen sind."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TaskList erfolgreich archiviert",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "TaskList nicht gefunden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "TaskList konnte nicht archiviert werden (Tasks nicht abgeschlossen)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)))
    })
    @PutMapping(value = "/tasklists/{id}/archive", produces = "application/json")
    ResponseEntity<APIResponse<TaskListDto>> archiveTaskList(@Parameter(description = "UUID der TaskList") UUID id);
}
