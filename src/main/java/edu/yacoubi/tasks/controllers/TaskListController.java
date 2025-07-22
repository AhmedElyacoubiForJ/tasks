package edu.yacoubi.tasks.controllers;

import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskSummaryDto;
import edu.yacoubi.tasks.mappers.TaskListMapper;
import edu.yacoubi.tasks.servcies.ITaskListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/task-lists")
@RequiredArgsConstructor
public class TaskListController implements TaskListControllerSwaggerDocs {

    private final ITaskListService taskListService;
    private final TaskListMapper taskListMapper;

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
