package edu.yacoubi.tasks.controllers.api.contract;

import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseListTaskListDto;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseTaskListDto;

import edu.yacoubi.tasks.controllers.api.annotations.DomainErrorResponses;
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
        name = "TaskLists – Szenarien",
        description = "Spezial- und Szenario-Endpunkte wie Archivierung oder Statusfilter"
)
public interface ITaskListsScenarioApi extends IApiPrefix {

    @Operation(
            summary = "Aktive TaskLists abrufen",
            description = "Gibt eine Liste aller aktiven TaskLists zurück."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste aller aktiven TaskLists",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponseListTaskListDto.class)
            )
    )
    @GetMapping(value = "/tasklists/active", produces = "application/json")
    ResponseEntity<APIResponseListTaskListDto> getActiveTaskLists();

    @Operation(
            summary = "Archivierte TaskLists abrufen",
            description = "Gibt eine Liste aller archivierten TaskLists zurück."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste aller archivierten TaskLists",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponseListTaskListDto.class)
            )
    )
    @GetMapping(value = "/tasklists/archived", produces = "application/json")
    ResponseEntity<APIResponseListTaskListDto> getArchivedTaskLists();

    @Operation(
            summary = "TaskList archivieren",
            description = "Archiviert eine TaskList, wenn alle enthaltenen Tasks abgeschlossen sind."
    )
    @DomainErrorResponses
    @ApiResponse(
            responseCode = "200",
            description = "TaskList erfolgreich archiviert",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = APIResponseTaskListDto.class)
            )
    )
    @PutMapping(value = "/tasklists/{id}/archive", produces = "application/json")
    ResponseEntity<APIResponseTaskListDto> archiveTaskList(
            @Parameter(description = "UUID der TaskList")
            @PathVariable("id") UUID id
    );
}
