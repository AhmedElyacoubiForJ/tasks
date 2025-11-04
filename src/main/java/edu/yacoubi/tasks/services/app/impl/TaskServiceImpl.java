package edu.yacoubi.tasks.services.app.impl;

import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import edu.yacoubi.tasks.repositories.TaskRepository;
import edu.yacoubi.tasks.services.app.ITaskService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements ITaskService {

    private final TaskRepository taskRepository;

    @Override
    public List<TaskSummaryDto> findByTaskListId(@NotNull UUID taskListId) {
        log.info("📥 Lade Tasks für TaskList-ID: {}", taskListId);
        List<TaskSummaryDto> tasks = taskRepository.findByTaskListId(taskListId);

        if (tasks.isEmpty()) {
            log.info("📭 Keine Tasks gefunden für TaskList-ID: {}", taskListId);
        } else {
            log.info("📦 {} Tasks geladen für TaskList-ID: {}", tasks.size(), taskListId);
        }

        return tasks;
    }

    @Override
    public List<TaskSummaryDto> findByTaskListIdAndStatus(UUID taskListId, TaskStatus status) {
        log.info("📥 Lade Tasks mit Status {} für TaskList {}", status, taskListId);

        List<TaskSummaryDto> tasks = taskRepository.findByTaskListIdAndStatus(taskListId, status);

        log.info("📦 {} Tasks mit Status {} geladen für TaskList {}", tasks.size(), status, taskListId);
        return tasks;
    }
}
