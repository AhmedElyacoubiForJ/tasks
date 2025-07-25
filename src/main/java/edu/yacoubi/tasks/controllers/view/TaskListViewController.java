package edu.yacoubi.tasks.controllers.view;

import edu.yacoubi.tasks.domain.dto.request.tasklist.CreateTaskListDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.mappers.TaskListMapper;
import edu.yacoubi.tasks.services.app.ITaskListService;
import edu.yacoubi.tasks.services.ui.IProgressColorService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class TaskListViewController {

    private final ITaskListService taskListService;
    private final TaskListMapper taskListMapper;
    private final IProgressColorService progressColorService;

    @GetMapping("/tasklists")
    public String showTaskLists(Model model) {
        List<TaskList> lists = taskListService.listTaskLists();
        List<TaskListDto> dtos = lists.stream()
                .map(taskListMapper::toTaskListDto)
                .toList();

        model.addAttribute("taskLists", dtos);
        model.addAttribute("progressColorService", progressColorService);


        return "tasklists/list";
    }

    @GetMapping("/tasklists/new")
    public String showTaskListForm(Model model) {
        model.addAttribute("createTaskListDto", new CreateTaskListDto("", ""));
        System.out.println("TaskListForm: " + model.getAttribute("createTaskListDto"));
        return "tasklists/form :: formFragment"; // Nur das Fragment
    }

    @PostMapping("/tasklists")
    public String createTaskList(@Valid CreateTaskListDto dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("createTaskListDto", dto);
            return "tasklists/form";
        }

        taskListService.createTaskList(dto);

        return "redirect:/tasklists";
    }

    @DeleteMapping("/tasklists/delete/{id}")
    public String deleteTaskList(@PathVariable UUID id) {
        System.out.println("Deleting TaskList with ID: " + id);
        taskListService.deleteTaskList(id);
        return "redirect:/tasklists"; // Navigiert zu HTML-Seite
    }
}
