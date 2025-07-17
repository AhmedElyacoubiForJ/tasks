package edu.yacoubi.tasks.domain.dto.request.tasklist;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskListDto(
        @NotBlank String title,
        String description
) {}
