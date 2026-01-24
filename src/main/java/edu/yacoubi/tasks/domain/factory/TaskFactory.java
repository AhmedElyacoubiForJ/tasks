package edu.yacoubi.tasks.domain.factory;

import edu.yacoubi.tasks.domain.dto.request.task.CreateTaskDto;
import edu.yacoubi.tasks.domain.entities.Task;
import edu.yacoubi.tasks.domain.entities.TaskList;

public final class TaskFactory {

    private TaskFactory() {
        // Utility-Klasse – keine Instanzen
    }

    /**
     * Fabrik-Methode zur Erstellung einer neuen {@link Task}-Entity
     * aus einem {@link CreateTaskDto} und einer bereits geladenen {@link TaskList}.
     *
     * <p>Die TaskList wird NICHT in dieser Methode geladen, sondern vom aufrufenden
     * Orchestrator bzw. TaskList-Service bereitgestellt. So bleiben Aggregatsgrenzen
     * gewahrt und Datenbankzugriffe klar im Service-Layer verankert.</p>
     *
     * <p><strong>Wichtig:</strong>
     * Diese Methode ist ausschließlich für die Erstellung neuer Tasks gedacht.
     * Updates an bestehenden Tasks erfolgen über Domain-Methoden der Task-Entity
     * (z.B. {@code changePriority()}, {@code complete()}, {@code reopen()}), nicht
     * über Mapping.</p>
     */
    public static Task create(CreateTaskDto dto, TaskList taskList) {
        return Task.builder()
                .title(dto.title())
                .description(dto.description())
                .dueDate(dto.dueDate())
                .priority(dto.priority())
                .taskList(taskList)
                .build();
    }
}

