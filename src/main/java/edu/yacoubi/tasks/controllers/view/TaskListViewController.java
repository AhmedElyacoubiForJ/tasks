package edu.yacoubi.tasks.controllers.view;

import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.mappers.TaskListMapper;
import edu.yacoubi.tasks.services.ITaskListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TaskListViewController {

    private final ITaskListService taskListService;
    private final TaskListMapper taskListMapper;

    @GetMapping("/tasklists")
    public String showTaskLists(Model model) {
        List<TaskList> lists = taskListService.listTaskLists();
        List<TaskListDto> dtos = lists.stream()
                .map(taskListMapper::toTaskListDto)
                .toList();

        model.addAttribute("taskLists", dtos);

        return "views/tasklists"; // verweist auf src/main/resources/templates/views/tasklists.html
    }
}
