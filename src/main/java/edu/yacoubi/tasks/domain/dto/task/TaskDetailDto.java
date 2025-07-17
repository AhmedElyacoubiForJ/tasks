package edu.yacoubi.tasks.domain.dto.task;

import java.time.LocalDateTime;
import java.util.UUID;

// komplette Ansicht
public record TaskDetailDto(
        UUID id,
        String title,
        String description,
        LocalDateTime dueDate,
        String status,
        String priority,
        UUID taskListId,
        LocalDateTime created,
        LocalDateTime updated
) {
}
