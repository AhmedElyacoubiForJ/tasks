package edu.yacoubi.tasks.application.ports;

import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.task.CreateTaskDto;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.task.FullUpdateTaskDto;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.task.PatchTaskDto;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.model.TaskList;
import edu.yacoubi.tasks.domain.model.enums.TaskStatus;

import java.util.UUID;

public interface ITaskListsTaskOrchestrator {

  TaskSummaryDto createTaskInList(UUID taskListId, CreateTaskDto dto);

  TaskSummaryDto updateTaskInList(UUID taskListId, UUID taskId, FullUpdateTaskDto dto);

  TaskSummaryDto patchTaskInList(UUID taskListId, UUID taskId, PatchTaskDto dto);

  void deleteTaskInList(UUID taskListId, UUID taskId);

  TaskList archiveTaskList(UUID taskListId);

  TaskList changeTaskStatus(UUID taskListId, UUID taskId, TaskStatus taskStatus);
}
