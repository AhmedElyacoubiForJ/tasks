package edu.yacoubi.tasks.controllers.view;

import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import edu.yacoubi.tasks.services.app.ITaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final ITaskService taskService;

    @GetMapping("/tasklists/{taskListId}/tasks")
    public String listTasks(@PathVariable UUID taskListId, Model model) {
        log.info("📥 Lade alle Tasks für TaskList {}", taskListId);

        List<TaskSummaryDto> tasks = taskService.findByTaskListId(taskListId);

        model.addAttribute("taskListId", taskListId);
        model.addAttribute("selectedStatus", null); // Kein Filter aktiv
        model.addAttribute("tasks", tasks);

        return "tasks/list";
    }

    @GetMapping("/tasklists/{taskListId}/tasks/status")
    public String listTasksByStatus(@PathVariable UUID taskListId,
                                    @RequestParam TaskStatus status,
                                    Model model) {
        log.info("📥 Lade Tasks mit Status {} für TaskList {}", status, taskListId);

        List<TaskSummaryDto> tasks = taskService.findByTaskListIdAndStatus(taskListId, status);

        model.addAttribute("taskListId", taskListId);
        model.addAttribute("selectedStatus", status.name());
        model.addAttribute("tasks", tasks);

        return "tasks/list";
    }



/*
    @PostMapping
    public String createTask(@PathVariable UUID taskListId,
                             @Valid @ModelAttribute("task") CreateTaskDto dto,
                             BindingResult bindingResult,
                             Model model) {
        return null;
    }

    @PutMapping("/{taskId}")
    public String updateTask(...) {
        ...
    }

    @DeleteMapping("/{taskId}")
    public String deleteTask(...) {
        ...
    }*/
}

