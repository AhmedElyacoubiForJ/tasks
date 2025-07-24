package edu.yacoubi.tasks.controllers;

import edu.yacoubi.tasks.domain.dto.request.tasklist.TaskListFilterDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "TaskLists", description = "Verwaltung von Task-Listen")
public interface TaskListControllerSwaggerDocs {
    @Operation(summary = "Alle TaskLists mit Details", description = "Liefert vollständige Informationen inkl. Tasks und Fortschritt")
    @ApiResponse(responseCode = "200", description = "Liste erfolgreich zurückgegeben")
    @ApiResponse(responseCode = "500", description = "Interner Serverfehler")
    ResponseEntity<APIResponse<List<TaskListWithTaskDetailDto>>> getDetailedLists();

    @Operation(summary = "Alle TaskLists mit Zusammenfassung", description = "Liefert eine Übersicht der TaskLists mit Anzahl und Fortschritt")
    @ApiResponse(responseCode = "200", description = "Liste erfolgreich zurückgegeben")
    @ApiResponse(responseCode = "500", description = "Interner Serverfehler")
    ResponseEntity<APIResponse<List<TaskListWithTaskSummaryDto>>> getSummarizedLists();

    @Operation(
            summary = "Task-Listen abrufen mit optionaler Paginierung und Filterung",
            description = "Liefert Task-Listen nach optionalem Titel-/Beschreibung-Filter und unterstützt Paging über page & size"
    )
    @ApiResponse(responseCode = "200", description = "Task-Listen erfolgreich zurückgegeben")
    @ApiResponse(responseCode = "400", description = "Ungültige Anfrageparameter")
    @ApiResponse(responseCode = "500", description = "Interner Serverfehler")
    ResponseEntity<APIResponse<Page<TaskListDto>>> getTaskLists(@ParameterObject TaskListFilterDto filterParams);

}
