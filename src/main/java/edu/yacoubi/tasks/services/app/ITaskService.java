package edu.yacoubi.tasks.services.app;

import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.Task;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface ITaskService {

  Task getTaskOrThrow(@NotNull UUID taskId);

  Task save(Task task);

  TaskSummaryDto toSummaryDto(Task task);
}
