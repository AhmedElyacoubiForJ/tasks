package edu.yacoubi.tasks.controllers;

import edu.yacoubi.tasks.domain.dto.request.tasklist.TaskListFilterDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskSummaryDto;
import edu.yacoubi.tasks.mappers.TaskListMapper;
import edu.yacoubi.tasks.servcies.ITaskListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/task-lists")
@RequiredArgsConstructor
@Slf4j
public class TaskListController implements TaskListControllerSwaggerDocs {

    private final ITaskListService taskListService;
    private final TaskListMapper taskListMapper;

    @GetMapping
    public ResponseEntity<APIResponse<Page<TaskListDto>>> getTaskLists(
            @ParameterObject TaskListFilterDto filterParams
    ) {
        Page<TaskListDto> result = taskListService.getFilteredTaskLists(filterParams);
        return ResponseEntity.ok(APIResponse.<Page<TaskListDto>>builder()
                .status("success")
                .statusCode(200)
                .message("Task-Listen erfolgreich zurückgegeben")
                .data(result)
                .build()
        );
    }

    @GetMapping("/details")
    public ResponseEntity<APIResponse<List<TaskListWithTaskDetailDto>>> getDetailedLists() {
        log.info("GET /task-lists/details angefragt");
        List<TaskListWithTaskDetailDto> result = taskListService.listTaskLists().stream()
                .map(taskListMapper::toWithTaskDetailDto)
                .toList();

        log.debug("Gefundene TaskLists: {}", result.size());
        APIResponse<List<TaskListWithTaskDetailDto>> response =
                APIResponse.<List<TaskListWithTaskDetailDto>>builder()
                        .status("success")
                        .statusCode(HttpStatus.OK.value())
                        .message("Liste erfolgreich zurückgegeben")
                        .data(result)
                        .build();

        log.info("GET /task-lists/details erfolgreich abgeschlossen");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<APIResponse<List<TaskListWithTaskSummaryDto>>> getSummarizedLists() {
        log.info("GET /task-lists/summary angefragt");
        List<TaskListWithTaskSummaryDto> result = taskListService.listTaskLists().stream()
                .map(taskListMapper::toWithTaskSummaryDto)
                .toList();

        log.debug("Gefundene TaskLists: {}", result.size());
        APIResponse<List<TaskListWithTaskSummaryDto>> response =
                APIResponse.<List<TaskListWithTaskSummaryDto>>builder()
                        .status("success")
                        .statusCode(HttpStatus.OK.value())
                        .message("Zusammenfassung erfolgreich zurückgegeben")
                        .data(result)
                        .build();

        log.info("GET /task-lists/summary erfolgreich abgeschlossen");
        return ResponseEntity.ok(response);
    }

}
