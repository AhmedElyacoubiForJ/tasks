package edu.yacoubi.tasks.controllers;

import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskSummaryDto;
import edu.yacoubi.tasks.mappers.TaskListMapper;
import edu.yacoubi.tasks.servcies.ITaskListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    @GetMapping("/details")
    public List<TaskListWithTaskDetailDto> getDetailedLists() {
        return taskListService.listTaskLists().stream()
                .map(taskListMapper::toWithTaskDetailDto)
                .toList();
    }

    @Operation(
            summary = "Alle TaskLists mit Zusammenfassung",
            description = "Liefert eine Übersicht der TaskLists mit Anzahl und Fortschritt"
    )
    @ApiResponse(responseCode = "200", description = "Liste erfolgreich zurückgegeben")
    @GetMapping("/summary")
    public List<TaskListWithTaskSummaryDto> getSummarizedLists() {
        return taskListService.listTaskLists().stream()
                .map(taskListMapper::toWithTaskSummaryDto)
                .toList();
    }
}
