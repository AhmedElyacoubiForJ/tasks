package edu.yacoubi.tasks.controllers.view;

import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import edu.yacoubi.tasks.services.app.ITaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final ITaskService taskService;

    @GetMapping(value = "/tasklists/{taskListId}/tasks", produces = "text/html")
    public String listTasks(@PathVariable UUID taskListId,
                            @RequestHeader(value = "HX-Request", required = false) String hx,
                            Model model) {
        log.info("📥 Lade alle Tasks für TaskList {}", taskListId);

        List<TaskSummaryDto> tasks = taskService.findByTaskListId(taskListId);

        model.addAttribute("taskListId", taskListId);
        model.addAttribute("selectedStatus", null); // Kein Filter aktiv
        model.addAttribute("tasks", tasks);

        // Der Controller soll erkennen, ob die Anfrage von HTMX kommt
        // — und dann nur das Tabellen-Fragment zurückgeben, statt die ganze Seite

        return (hx != null) ? "tasks/list :: table" : "tasks/list";
    }

//    @GetMapping(value = "/tasklists/{taskListId}/tasks/status", produces = "text/html")
//    public String listTasksByStatus(@PathVariable UUID taskListId,
//                                    @RequestParam TaskStatus status,
//                                    @RequestHeader(value = "HX-Request", required = false) String hx,
//                                    Model model) {
//        log.info("📥 Lade Tasks mit Status {} für TaskList {}", status, taskListId);
//
//        List<TaskSummaryDto> tasks = taskService.findByTaskListIdAndStatus(taskListId, status);
//
//        model.addAttribute("taskListId", taskListId);
//        model.addAttribute("selectedStatus", status.name());
//        model.addAttribute("tasks", tasks);
//
//        return (hx != null) ? "tasks/list :: table" : "tasks/list";
//    }

//    @PatchMapping(value = "/tasks/{id}/status", produces = "text/html")
//    public String updateTaskStatus(@PathVariable UUID id,
//                                   @RequestParam("status") TaskStatus status,
//                                   @RequestHeader(value = "HX-Request", required = false) String hx,
//                                   Model model) {
//        // 🛠️ Service aufrufen
//        TaskSummaryDto updated = taskService.updateStatus(id, status);
//        model.addAttribute("task", updated);
//
//        // 🎯 HTMX-Request → nur Zeile zurückgeben
//        if (hx != null) {
//            return "tasks/fragments/task-row :: row";
//        }
//
//        // 🌍 Normaler Request → Redirect auf TaskList-Seite
//        return "redirect:/tasklists/" + updated.taskListId() + "/tasks";
//    }

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

