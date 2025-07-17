package edu.yacoubi.tasks.domain.dto.tasklist;

import java.util.UUID;

public record UpdateTaskListDto(
        UUID id,
        String title,
        String description
) {}
