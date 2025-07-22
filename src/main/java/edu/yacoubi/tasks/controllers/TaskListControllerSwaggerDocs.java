package edu.yacoubi.tasks.controllers;

import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
}
