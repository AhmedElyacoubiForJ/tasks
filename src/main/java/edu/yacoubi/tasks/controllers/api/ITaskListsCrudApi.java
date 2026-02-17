package edu.yacoubi.tasks.controllers.api;

import edu.yacoubi.tasks.domain.dto.request.tasklist.CreateTaskListDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.UpdateTaskListDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

/** CRUD-Endpunkte für TaskLists. Erbt zentrale Metadaten aus IBaseTaskListsApi. */
@Tag(
        name = "TaskLists – CRUD",
        description = "Basisoperationen für TaskLists: erstellen, abrufen, aktualisieren, löschen"
)
public interface ITaskListsCrudApi extends IApiPrefix {

    @Operation(
            summary = "Alle TaskLists abrufen",
            description = "Gibt eine Liste aller vorhandenen TaskLists zurück. Leere Liste, wenn keine vorhanden."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste aller TaskLists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)))
    })
    @GetMapping(value = "/tasklists", produces = "application/json")
    ResponseEntity<APIResponse<List<TaskListDto>>> getAllTaskLists();

    @Operation(
            summary = "TaskList anhand der UUID abrufen",
            description = "Gibt eine einzelne TaskList zurück, wenn die UUID existiert."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TaskList gefunden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "TaskList nicht gefunden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)))
    })
    @GetMapping(value = "/tasklists/{id}", produces = "application/json")
    ResponseEntity<APIResponse<TaskListDto>> getTaskList(
            @Parameter(name = "id", description = "UUID der TaskList", required = true)
            @PathVariable("id") UUID id
    );

    @Operation(
            summary = "Neue TaskList erstellen",
            description = "Erstellt eine neue TaskList. Title ist erforderlich, Description optional."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "TaskList erfolgreich erstellt",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe (z. B. fehlender oder leerer Titel)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)))
    })
    @PostMapping(value = "/tasklists", consumes = "application/json", produces = "application/json")
    ResponseEntity<APIResponse<TaskListDto>> createTaskList(
            @Parameter(description = "Daten für neue TaskList (Title erforderlich)")
            @Valid @RequestBody CreateTaskListDto dto
    );

    @Operation(
            summary = "TaskList vollständig aktualisieren (Title erforderlich)",
            description = "Ersetzt die vorhandene TaskList vollständig. Title ist erforderlich; Description optional."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TaskList erfolgreich aktualisiert",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe (z. B. leerer Titel)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "TaskList nicht gefunden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)))
    })
    @PutMapping(value = "/tasklists/{id}", consumes = "application/json", produces = "application/json")
    ResponseEntity<APIResponse<TaskListDto>> updateTaskList(
            @Parameter(description = "UUID der TaskList", required = true)
            @PathVariable("id") UUID id,
            @Parameter(description = "Daten für vollständiges Update (Title erforderlich)")
            @Valid @RequestBody UpdateTaskListDto dto
    );

    @Operation(
            summary = "TaskList löschen anhand der UUID",
            description = "Löscht eine bestehende TaskList. Gibt ein APIResponse<Void> zurück."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TaskList erfolgreich gelöscht",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "TaskList nicht gefunden",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "500", description = "Interner Serverfehler",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)))
    })
    @DeleteMapping(value = "/tasklists/{id}", produces = "application/json")
    ResponseEntity<APIResponse<Void>> deleteTaskList(
            @Parameter(description = "UUID der TaskList", required = true)
            @PathVariable UUID id
    );
}