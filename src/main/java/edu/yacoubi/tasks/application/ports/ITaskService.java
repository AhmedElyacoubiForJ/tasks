package edu.yacoubi.tasks.application.ports;

import edu.yacoubi.tasks.controllers.api.v1.contract.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.model.Task;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface ITaskService {

  Task getTaskOrThrow(@NotNull UUID taskId);

  Task save(Task task);

  TaskSummaryDto toSummaryDto(Task task);
}
