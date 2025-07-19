package edu.yacoubi.tasks.domain.dto.response.tasklist;

import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;

import java.util.List;
import java.util.UUID;

public record TaskListWithTaskSummaryDto(
        UUID id,
        String title,
        String description,
        Integer count,
        Double progress,
        List<TaskSummaryDto> tasks
) {
}
