package edu.yacoubi.tasks.controllers;

import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListWithTaskSummaryDto;
import edu.yacoubi.tasks.mappers.TaskListMapper;
import edu.yacoubi.tasks.servcies.ITaskListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/task-lists")
@RequiredArgsConstructor
public class TaskListController {

    private final ITaskListService taskListService;
    private final TaskListMapper taskListMapper;

    @GetMapping("/details")
    public List<TaskListWithTaskDetailDto> getDetailedLists() {
        return taskListService.listTaskLists().stream()
                .map(taskListMapper::toWithTaskDetailDto)
                .toList();
    }

    @GetMapping("/summary")
    public List<TaskListWithTaskSummaryDto> getSummarizedLists() {
        return taskListService.listTaskLists().stream()
                .map(taskListMapper::toWithTaskSummaryDto)
                .toList();
    }
}
