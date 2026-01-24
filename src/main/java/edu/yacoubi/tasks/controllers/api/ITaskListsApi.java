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
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "TaskLists", description = "REST-Endpunkte für TaskLists")
@RequestMapping(path = "/api")
public interface TaskListsApi {

    // --- CRUD für TaskLists ---
    @Operation(
            summary = "Alle TaskLists abrufen",
            description = "Gibt eine Liste aller vorhandenen TaskLists zurück. Leere Liste, wenn keine vorhanden."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste aller TaskLists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Interner Serverfehler",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            )
    })
    @GetMapping(value = "/tasklists", produces = "application/json")
    ResponseEntity<APIResponse<List<TaskListDto>>> getAllTaskLists();


    @Operation(
            summary = "Neue TaskList erstellen",
            description = "Erstellt eine neue TaskList. Title ist erforderlich, Description optional."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "TaskList erfolgreich erstellt",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ungültige Eingabe (z. B. fehlender oder leerer Titel)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Interner Serverfehler",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            )
    })
    @PostMapping(
            value = "/tasklists",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<APIResponse<TaskListDto>> createTaskList(
            @Parameter(description = "Daten für neue TaskList (Title erforderlich)")
            @Valid @RequestBody CreateTaskListDto dto
    );


    @Operation(
            summary = "TaskList anhand der UUID abrufen",
            description = "Gibt eine einzelne TaskList zurück, wenn die UUID existiert."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "TaskList gefunden",
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
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Interner Serverfehler",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            )
    })
    @GetMapping(value = "/tasklists/{id}", produces = "application/json")
    ResponseEntity<APIResponse<TaskListDto>> getTaskList(
            @Parameter(description = "UUID der TaskList", required = true)
            @PathVariable UUID id
    );


    @Operation(
            summary = "TaskList vollständig aktualisieren (Title erforderlich)",
            description = "Ersetzt die vorhandene TaskList vollständig. Title ist erforderlich; Description optional."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "TaskList erfolgreich aktualisiert",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ungültige Eingabe (z. B. leerer Titel)",
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
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Interner Serverfehler",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            )
    })
    @PutMapping(
            value = "/tasklists/{id}",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<APIResponse<TaskListDto>> updateTaskList(
            @Parameter(description = "UUID der TaskList", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Daten für vollständiges Update (Title erforderlich)")
            @Valid @RequestBody UpdateTaskListDto dto
    );


//    @Operation(
//            summary = "TaskList teilweise aktualisieren (nur übergebene Felder)",
//            description = "Aktualisiert nur die bereitgestellten Felder. Nicht gesendete Felder bleiben unverändert."
//    )
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "TaskList teilweise aktualisiert",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = APIResponse.class)
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Ungültige Eingabe (z. B. leerer Titel, falls mitgesendet)",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = APIResponse.class)
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "TaskList nicht gefunden",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = APIResponse.class)
//                    )
//            )
//    })
//    @PatchMapping(
//            value = "/tasklists/{id}",
//            consumes = "application/json",
//            produces = "application/json"
//    )
//    ResponseEntity<APIResponse<TaskListDto>> patchTaskList(
//            @Parameter(description = "UUID der TaskList", required = true)
//            @PathVariable UUID id,
//            @Parameter(description = "Teil-Update: nur gesendete Felder werden übernommen")
//            @Valid @RequestBody PatchTaskListDto dto
//    );



    @Operation(
            summary = "TaskList löschen anhand der UUID",
            description = "Löscht eine bestehende TaskList. Gibt ein APIResponse<Void> zurück."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "TaskList erfolgreich gelöscht",
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
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Interner Serverfehler",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            )
    })
    @DeleteMapping(
            value = "/tasklists/{id}",
            produces = "application/json"
    )
    ResponseEntity<APIResponse<Void>> deleteTaskList(
            @Parameter(description = "UUID der TaskList", required = true)
            @PathVariable UUID id
    );
}
    // --- Subresource: Tasks innerhalb einer TaskList ---
//    @Operation(summary = "Alle Tasks einer TaskList abrufen")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Liste aller Tasks"),
//            @ApiResponse(responseCode = "404", description = "TaskList nicht gefunden")
//    })
//    @GetMapping("/tasklists/{id}/tasks")
//    ResponseEntity<APIResponse<List<TaskDto>>> getTasksByListId(@PathVariable UUID id);
//
//    @Operation(summary = "Neuen Task in einer TaskList erstellen")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "Task erfolgreich erstellt"),
//            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe"),
//            @ApiResponse(responseCode = "404", description = "TaskList nicht gefunden")
//    })
//    @PostMapping("/tasklists/{id}/tasks")
//    ResponseEntity<APIResponse<TaskDto>> createTaskInList(@PathVariable UUID id, @Valid @RequestBody TaskDto dto);

//    @Operation(summary = "Task innerhalb einer TaskList aktualisieren")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Task erfolgreich aktualisiert"),
//            @ApiResponse(responseCode = "404", description = "Task oder TaskList nicht gefunden"),
//            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe")
//    })
//    @PutMapping("/tasklists/{id}/tasks/{taskId}")
//    ResponseEntity<APIResponse<TaskDto>> updateTaskInList(
//            @PathVariable UUID id,
//            @PathVariable UUID taskId,
//            @Valid @RequestBody TaskDto dto
//    );

//    @Operation(summary = "Task innerhalb einer TaskList löschen")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Task erfolgreich gelöscht"),
//            @ApiResponse(responseCode = "404", description = "Task oder TaskList nicht gefunden")
//    })
//    @DeleteMapping("/tasklists/{id}/tasks/{taskId}")
//    ResponseEntity<APIResponse<Void>> deleteTaskInList(@PathVariable UUID id, @PathVariable UUID taskId);

//    @Operation(summary = "Alle TaskLists abrufen")
//    @GetMapping("/tasklists")
//    ResponseEntity<APIResponse<List<TaskListSummaryDto>>> getAllTaskLists();
//
//    @Operation(summary = "Eine TaskList abrufen")
//    @GetMapping("/tasklists/{id}")
//    ResponseEntity<APIResponse<TaskListSummaryDto>> getTaskList(@PathVariable UUID id);
//
//    @Operation(summary = "Neue TaskList erstellen")
//    @PostMapping("/tasklists")
//    ResponseEntity<APIResponse<TaskListSummaryDto>> createTaskList(@Valid @RequestBody TaskListCreateDto dto);
//
//    @Operation(summary = "TaskList aktualisieren")
//    @PutMapping("/tasklists/{id}")
//    ResponseEntity<APIResponse<TaskListSummaryDto>> updateTaskList(
//            @PathVariable UUID id,
//            @Valid @RequestBody TaskListUpdateDto dto);
//
//    @Operation(summary = "TaskList löschen")
//    @DeleteMapping("/tasklists/{id}")
//    ResponseEntity<APIResponse<Void>> deleteTaskList(@PathVariable UUID id);


//    @Operation(summary = "Liste aller Tasks für eine TaskList")
//    @GetMapping("/tasklists/{id}/tasks")
//    ResponseEntity<APIResponse<List<TaskSummaryDto>>> getTasksByList(@PathVariable UUID id);
//
//    @Operation(summary = "Neue Task erstellen")
//    @PostMapping("/tasks")
//    ResponseEntity<APIResponse<TaskSummaryDto>> createTask(@Valid @RequestBody TaskCreateDto dto);


//    @Operation(summary = "Alle TaskLists mit Details", description = "Liefert vollständige Informationen inkl. Tasks und Fortschritt")
//    @ApiResponse(responseCode = "200", description = "Liste erfolgreich zurückgegeben")
//    @ApiResponse(responseCode = "500", description = "Interner Serverfehler")
//    ResponseEntity<APIResponse<List<TaskListWithTaskDetailDto>>> getDetailedLists();
//
//    @Operation(
//            summary = "Task-Listen abrufen mit optionaler Paginierung und Filterung",
//            description = "Liefert Task-Listen nach optionalem Titel-/Beschreibung-Filter und unterstützt Paging über page & size"
//    )
//    @ApiResponse(responseCode = "200", description = "Task-Listen erfolgreich zurückgegeben")
//    @ApiResponse(responseCode = "400", description = "Ungültige Anfrageparameter")
//    @ApiResponse(responseCode = "500", description = "Interner Serverfehler")
//    ResponseEntity<APIResponse<Page<TaskListDto>>> getTaskLists(@ParameterObject TaskListFilterDto filterParams);
//
//    @Operation(summary = "Alle TaskLists mit Zusammenfassung", description = "Liefert eine Übersicht der TaskLists mit Anzahl und Fortschritt")
//    @ApiResponse(responseCode = "200", description = "Liste erfolgreich zurückgegeben")
//    @ApiResponse(responseCode = "500", description = "Interner Serverfehler")
//    ResponseEntity<APIResponse<List<TaskListWithTaskSummaryDto>>> getSummarizedLists();