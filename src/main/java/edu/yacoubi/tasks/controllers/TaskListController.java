package edu.yacoubi.tasks.controllers;

import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskSummaryDto;
import edu.yacoubi.tasks.exceptions.ApiErrorResponse;
import edu.yacoubi.tasks.mappers.TaskListMapper;
import edu.yacoubi.tasks.servcies.ITaskListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "TaskLists", description = "Verwaltung von Task-Listen")
@RestController
@RequestMapping(path = "/task-lists")
@RequiredArgsConstructor
public class TaskListController {

    private final ITaskListService taskListService;
    private final TaskListMapper taskListMapper;

    @Operation(
            summary = "Alle TaskLists mit Details",
            description = "Liefert vollständige Informationen inkl. Tasks und Fortschritt"
    )
    @ApiResponse(responseCode = "200", description = "Liste erfolgreich zurückgegeben")
    @ApiResponse(
            responseCode = "500", description = "Interner Serverfehler",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)
            )
    )
    @GetMapping("/details")
    public ResponseEntity<APIResponse<List<TaskListWithTaskDetailDto>>> getDetailedLists() {
        List<TaskListWithTaskDetailDto> result = taskListService.listTaskLists().stream()
                .map(taskListMapper::toWithTaskDetailDto)
                .toList();

        APIResponse<List<TaskListWithTaskDetailDto>> response = APIResponse.<List<TaskListWithTaskDetailDto>>builder()
                .status("success")
                .statusCode(HttpStatus.OK.value())
                .message("Liste erfolgreich zurückgegeben")
                .data(result)
                .build();

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Alle TaskLists mit Zusammenfassung",
            description = "Liefert eine Übersicht der TaskLists mit Anzahl und Fortschritt"
    )
    @ApiResponse(responseCode = "200", description = "Liste erfolgreich zurückgegeben")
    @ApiResponse(
            responseCode = "500",
            description = "Interner Serverfehler",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @GetMapping("/summary")
    public ResponseEntity<APIResponse<List<TaskListWithTaskSummaryDto>>> getSummarizedLists() {
        List<TaskListWithTaskSummaryDto> result = taskListService.listTaskLists().stream()
                .map(taskListMapper::toWithTaskSummaryDto)
                .toList();

        APIResponse<List<TaskListWithTaskSummaryDto>> response = APIResponse.<List<TaskListWithTaskSummaryDto>>builder()
                .status("success")
                .statusCode(HttpStatus.OK.value())
                .message("Zusammenfassung erfolgreich zurückgegeben")
                .data(result)
                .build();

        return ResponseEntity.ok(response);
    }

}
