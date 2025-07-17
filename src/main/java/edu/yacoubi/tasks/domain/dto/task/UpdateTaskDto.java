package edu.yacoubi.tasks.domain.dto.task;

import java.util.UUID;
import java.time.LocalDateTime;

public record UpdateTaskDto(
        UUID id,
        String title,
        String description,
        LocalDateTime dueDate,
        String status,
        String priority
) {}
