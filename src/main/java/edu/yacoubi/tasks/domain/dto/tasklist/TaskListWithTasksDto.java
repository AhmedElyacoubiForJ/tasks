package edu.yacoubi.tasks.domain.dto.tasklist;

import edu.yacoubi.tasks.domain.dto.task.TaskSummaryDto;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

// inkl. verknüpfter Tasks, z.B. für Admin oder Web-Ansicht
public record TaskListWithTasksDto(
        UUID id,
        String title,
        String description,
        List<TaskSummaryDto> tasks,
        LocalDateTime created,
        LocalDateTime updated
) {}
