package edu.yacoubi.tasks.domain.dto.tasklist;

import java.time.LocalDateTime;
import java.util.UUID;

// Detailansicht ohne Tasks
public record TaskListDto(
        UUID id,
        String title,
        String description,
        LocalDateTime created,
        LocalDateTime updated
) {}
