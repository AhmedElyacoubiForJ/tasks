package edu.yacoubi.tasks.domain.dto.task;

import edu.yacoubi.tasks.domain.entities.TaskStatus;

import java.util.UUID;

// z.B. für Listenansicht / Mobile
public record TaskSummaryDto(
        UUID id,
        String title,
        TaskStatus status
) {}
