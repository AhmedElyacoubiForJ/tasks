package edu.yacoubi.tasks.domain.dto.request.task;

import edu.yacoubi.tasks.domain.entities.TaskPriority;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateTaskDto(
        @NotBlank String title,
        String description,
        @FutureOrPresent LocalDateTime dueDate,
        @NotNull TaskStatus status,
        @NotNull TaskPriority priority,
        @NotNull UUID taskListId
) {}
