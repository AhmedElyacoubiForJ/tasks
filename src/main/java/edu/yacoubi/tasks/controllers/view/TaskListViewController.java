package edu.yacoubi.tasks.controllers.view;

import edu.yacoubi.tasks.domain.dto.request.tasklist.CreateTaskListDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.UpdateTaskListDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.mappers.TaskListMapper;
import edu.yacoubi.tasks.services.app.ITaskListService;
import edu.yacoubi.tasks.services.ui.IProgressColorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/tasklists")
public class TaskListViewController {

    private final ITaskListService taskListService;
    private final TaskListMapper taskListMapper;
    private final IProgressColorService progressColorService;

    @GetMapping()
    public String showTaskLists(Model model) {
        List<TaskList> lists = taskListService.listTaskLists();
        List<TaskListDto> dtos = lists.stream()
                .map(taskListMapper::toTaskListDto)
                .toList();

        model.addAttribute("taskLists", dtos);
        model.addAttribute("progressColorService", progressColorService);

        return "tasklists/list";
    }

    @GetMapping("/new")
    public String showTaskListForm(Model model) {
        model.addAttribute("createTaskListDto", new CreateTaskListDto("", ""));
        System.out.println("TaskListForm: " + model.getAttribute("createTaskListDto"));
        return "tasklists/form :: formFragment"; // Nur das Fragment
    }

    @PostMapping()
    public String createTaskList(@Valid CreateTaskListDto dto,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("createTaskListDto", dto);
            return "tasklists/form";
        }

        taskListService.createTaskList(dto);

        return "redirect:/tasklists";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskList(@PathVariable UUID id) {
        taskListService.deleteTaskList(id);
        return ResponseEntity.ok().build(); // 200 OK, leerer Body
    }

    //
    // 🔁 Anzeigezeile zurückgeben (nach Speichern oder Abbrechen)
    @GetMapping("/{id}")
    public String getDisplayRow(@PathVariable UUID id, Model model) {
        log.info("❌ GET /tasklists/{} aufgerufen", id);
        TaskListDto dto = taskListMapper.toTaskListDto(taskListService.getTaskListOrThrow(id));
        model.addAttribute("taskList", dto);
        model.addAttribute("progressColorService", progressColorService);
        return "tasklists/fragments/tasklist-row :: row";
    }

    // ✏️ Bearbeitungszeile laden (DONE)
    @GetMapping("/{id}/edit")
    public String getEditRow(@PathVariable UUID id, Model model) {
        log.info("🔧 GET /tasklists/{}/edit aufgerufen", id);
        TaskList taskList = taskListService.getTaskListOrThrow(id);
        TaskListDto dto = taskListMapper.toTaskListDto(taskList);
        model.addAttribute("taskList", dto);
        model.addAttribute("progressColorService", progressColorService);
        return "tasklists/fragments/tasklist-edit-row :: row";
    }

    // 💾 Speichern der Änderungen (DONE)
    @PutMapping("/{id}")
    public String updateTaskList(@PathVariable UUID id,
                                 @Valid @ModelAttribute("taskList") UpdateTaskListDto dto,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("taskList", dto); // wichtig!
            model.addAttribute("progressColorService", progressColorService);
            return "tasklists/fragments/tasklist-edit-row :: row";
        }

        TaskList updated = taskListService.updateTaskList(id, dto);
        TaskListDto updatedDto = taskListMapper.toTaskListDto(updated);

        model.addAttribute("taskList", updatedDto);
        model.addAttribute("progressColorService", progressColorService);
        return "tasklists/fragments/tasklist-row :: row";
    }

    @GetMapping("/export/pdf/view")
    public String showPdfView(Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        List<Map<String, String>> formattedTaskLists = taskListService.listTaskLists().stream()
                .map(taskList -> {
                    TaskListDto dto = taskListMapper.toTaskListDto(taskList);
                    Map<String, String> map = new LinkedHashMap<>();
                    map.put("id", dto.id().toString());
                    map.put("title", dto.title());
                    map.put("description", dto.description());
                    map.put("created", formatter.format(dto.created()));
                    map.put("updated", formatter.format(dto.updated()));
                    map.put("progress", String.valueOf(dto.roundedProgress()));
                    return map;
                })
                .toList();

        model.addAttribute("taskLists", formattedTaskLists);
        model.addAttribute("exportDate", formatter.format(LocalDateTime.now()));

        return "tasklists/pdf-view";
    }

}
