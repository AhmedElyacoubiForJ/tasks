package edu.yacoubi.tasks.application.ports;

import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.tasklist.CreateTaskListDto;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.tasklist.PatchTaskListDto;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.tasklist.TaskListFilterDto;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.tasklist.UpdateTaskListDto;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.model.TaskList;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ITaskListService {

  List<TaskList> getAllTaskLists();

  List<TaskList> getActiveTaskLists();

  List<TaskList> getArchivedTaskLists();

  Page<TaskListDto> getFilteredTaskLists(TaskListFilterDto params);

  TaskList getTaskListOrThrow(UUID id);

  TaskList createTaskList(CreateTaskListDto taskListDto);

  void deleteTaskList(UUID id);

  TaskList updateTaskList(UUID id, UpdateTaskListDto dto);

  TaskList patchTaskList(UUID id, PatchTaskListDto dto);

  TaskList activateTaskList(UUID id);

  TaskList save(TaskList taskList);
}
