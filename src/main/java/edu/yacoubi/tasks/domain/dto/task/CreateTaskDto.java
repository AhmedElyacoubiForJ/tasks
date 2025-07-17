package edu.yacoubi.tasks.domain.dto.task;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateTaskDto(
        String title,
        String description,
        LocalDateTime dueDate,
        String status,      // Enum als String → evtl. später validieren
        String priority,    // Enum als String
        UUID taskListId
) {}
