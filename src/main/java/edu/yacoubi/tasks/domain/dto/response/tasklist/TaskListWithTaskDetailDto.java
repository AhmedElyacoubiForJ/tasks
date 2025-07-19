package edu.yacoubi.tasks.domain.dto.response.tasklist;

import edu.yacoubi.tasks.domain.dto.response.task.TaskDetailDto;

import java.util.List;
import java.util.UUID;

public record TaskListWithTaskDetailDto(
        UUID id,
        String title,
        String description,
        Integer count,
        Double progress,
        List<TaskDetailDto> tasks
) {
}
