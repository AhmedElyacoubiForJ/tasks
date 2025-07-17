package edu.yacoubi.tasks.domain.dto.tasklist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateTaskListDto(
        @NotNull UUID id,
        @NotBlank String title,
        String description
) {}
