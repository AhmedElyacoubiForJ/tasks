package edu.yacoubi.tasks.controllers.api.contract;

import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseListTaskListDto;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseTaskListDto;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseVoid;
import edu.yacoubi.tasks.domain.dto.request.tasklist.CreateTaskListDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.PatchTaskListDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.UpdateTaskListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(
        name = "TaskLists – CRUD",
        description = "Basisoperationen für TaskLists: erstellen, abrufen, aktualisieren, löschen"
)
public interface ITaskListsCrudApi extends IApiPrefix {

    @Operation(
            summary = "Alle TaskLists abrufen",
            description = "Gibt eine Liste aller vorhandenen TaskLists zurück."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste aller TaskLists",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponseListTaskListDto.class)
            )
    )
    @GetMapping(value = "/tasklists", produces = "application/json")
    ResponseEntity<APIResponseListTaskListDto> getAllTaskLists();

    @Operation(
            summary = "TaskList anhand der UUID abrufen",
            description = "Gibt eine einzelne TaskList zurück, wenn die UUID existiert."
    )
    @ApiResponse(
            responseCode = "200",
            description = "TaskList gefunden",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponseTaskListDto.class)
            )
    )
    @GetMapping(value = "/tasklists/{id}", produces = "application/json")
    ResponseEntity<APIResponseTaskListDto> getTaskList(
            @Parameter(description = "UUID der TaskList")
            @PathVariable("id") UUID id
    );

    @Operation(
            summary = "Neue TaskList erstellen",
            description = "Erstellt eine neue TaskList. Title ist erforderlich."
    )
    @ApiResponse(
            responseCode = "201",
            description = "TaskList erfolgreich erstellt",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponseTaskListDto.class)
            )
    )
    @PostMapping(value = "/tasklists", consumes = "application/json", produces = "application/json")
    ResponseEntity<APIResponseTaskListDto> createTaskList(
            @RequestBody CreateTaskListDto dto
    );

    @Operation(
            summary = "TaskList vollständig aktualisieren",
            description = "Ersetzt die vorhandene TaskList vollständig."
    )
    @ApiResponse(
            responseCode = "200",
            description = "TaskList erfolgreich aktualisiert",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponseTaskListDto.class)
            )
    )
    @PutMapping(value = "/tasklists/{id}", consumes = "application/json", produces = "application/json")
    ResponseEntity<APIResponseTaskListDto> updateTaskList(
            @PathVariable("id") UUID id,
            @RequestBody UpdateTaskListDto dto
    );

    @Operation(
            summary = "TaskList teilweise aktualisieren (PATCH)",
            description = "Aktualisiert nur die Felder, die im DTO gesetzt sind. "
                    + "Null-Werte werden ignoriert."
    )
    @ApiResponse(
            responseCode = "200",
            description = "TaskList erfolgreich aktualisiert (PATCH)",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponseTaskListDto.class)
            )
    )
    @PatchMapping(
            value = "/tasklists/{id}",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<APIResponseTaskListDto> patchTaskList(
            @Parameter(description = "UUID der TaskList")
            @PathVariable("id") UUID id,
            @RequestBody PatchTaskListDto dto
    );

    @Operation(
            summary = "TaskList löschen",
            description = "Löscht eine bestehende TaskList."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Task erfolgreich gelöscht",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponseVoid.class)
            )
    )
    @DeleteMapping(value = "/tasklists/{id}", produces = "application/json")
    ResponseEntity<APIResponseVoid> deleteTaskList(
            @PathVariable("id") UUID id
    );
}
