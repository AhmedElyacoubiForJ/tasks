package edu.yacoubi.tasks.controllers.view;

import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
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

        // TODO
        List<TaskSummaryDto> tasks = null;//taskService.findByTaskListId(taskListId);

        model.addAttribute("taskListId", taskListId);
        model.addAttribute("selectedStatus", null); // Kein Filter aktiv
        model.addAttribute("tasks", tasks);

        // Der Controller soll erkennen, ob die Anfrage von HTMX kommt
        // — und dann nur das Tabellen-Fragment zurückgeben, statt die ganze Seite

        return (hx != null) ? "tasks/list :: table" : "tasks/list";
    }
}

