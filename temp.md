package edu.yacoubi.tasks.services.app;

import edu.yacoubi.tasks.domain.dto.request.task.CreateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.FullUpdateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.PatchTaskDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.TaskList;

import java.util.UUID;

public interface ITaskListsTaskOrchestrator {
    TaskSummaryDto createTaskInList(UUID taskListId, CreateTaskDto dto);
    TaskSummaryDto updateTaskInList(UUID taskListId, UUID taskId, FullUpdateTaskDto dto);
    TaskSummaryDto patchTaskInList(UUID taskListId, UUID taskId, PatchTaskDto dto);
    void deleteTaskInList(UUID taskListId, UUID taskId);
    TaskList archiveTaskList(UUID taskListId);
}
