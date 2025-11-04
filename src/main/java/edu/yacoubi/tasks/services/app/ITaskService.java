package edu.yacoubi.tasks.services.app;

import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface ITaskService {
    List<TaskSummaryDto> findByTaskListId(@NotNull UUID taskListId);

    List<TaskSummaryDto> findByTaskListIdAndStatus(UUID taskListId, TaskStatus status);
}
