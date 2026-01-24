package edu.yacoubi.tasks.mappers;

import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.entities.Task;
import edu.yacoubi.tasks.domain.entities.TaskList;

/**
 * Zentrale Transformationslogik für TaskList-Entities.
 *
 * <p>Diese Klasse wandelt das TaskList-Aggregat in ein TaskListDto um.
 * Sie respektiert Aggregatsgrenzen, gibt keine verschachtelten Tasks zurück
 * und berechnet Metadaten wie Anzahl und Fortschritt.</p>
 */
public final class TaskListTransformer {

    private TaskListTransformer() {
        // Utility-Klasse – keine Instanzen
    }

    /**
     * Transformer: TaskList → TaskListDto
     *
     * <p>Berechnet:
     * - Anzahl der Tasks
     * - Fortschritt in Prozent (completed / total)
     * </p>
     */
    public static final ITransformer<TaskList, TaskListDto> TASKLIST_TO_DTO =
            taskList -> {
                int total = taskList.getTasks().size();
                long completed = taskList.getTasks()
                        .stream()
                        .filter(Task::isCompleted)
                        .count();

                double progress = total == 0
                        ? 0.0
                        : (completed * 100.0) / total;

                return new TaskListDto(
                        taskList.getId(),
                        taskList.getTitle(),
                        taskList.getDescription(),
                        taskList.getCreated(),
                        taskList.getUpdated(),
                        total,
                        progress
                );
            };
}