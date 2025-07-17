package edu.yacoubi.tasks.domain.dto.task;

import java.util.UUID;

// z.B. für Listenansicht / Mobile
public record TaskSummaryDto(
        UUID id,
        String title,
        String status
) {}
