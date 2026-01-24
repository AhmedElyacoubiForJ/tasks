package edu.yacoubi.tasks.services.app;

import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.Task;
import edu.yacoubi.tasks.domain.entities.TaskPriority;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Domain-Service für das Task-Aggregat.
 *
 * <p>Dieser Service enthält ausschließlich Operationen, die sich direkt auf das Task-Aggregat
 * beziehen. Cross-Aggregate-UseCases (z. B. Task in TaskList erstellen) werden im Orchestrator
 * gekapselt.
 *
 * <p>Der Service arbeitet ausschließlich mit Domain-Entities und Domain-Methoden. Setter-basierte
 * Mutationen oder DTO-basierte Updates sind nicht erlaubt.
 */
public interface ITaskService {

  Task getTaskOrThrow(@NotNull UUID taskId);

  /**
   * Liefert alle Tasks einer TaskList zurück.
   *
   * <p>Die Existenzprüfung der TaskList erfolgt im Orchestrator.
   */
  List<TaskSummaryDto> findByTaskListId(@NotNull UUID taskListId);

  /**
   * Liefert alle Tasks einer TaskList mit einem bestimmten Status.
   *
   * <p>Die Existenzprüfung der TaskList erfolgt im Orchestrator.
   */
  List<TaskSummaryDto> findByTaskListIdAndStatus(UUID taskListId, String status);

  /**
   * Erstellt einen neuen Task.
   *
   * <p>Die TaskList wird im Orchestrator gesetzt. Der Service speichert nur.
   */
  TaskSummaryDto createTask(Task task);

  TaskSummaryDto updateTask(Task task);

  //    /**
  //     * Markiert einen Task als abgeschlossen.
  //     */
  //    TaskSummaryDto completeTask(UUID taskId);
  //
  //    /**
  //     * Öffnet einen abgeschlossenen Task wieder.
  //     */
  //    TaskSummaryDto reopenTask(UUID taskId);

  /** Ändert die Priorität eines Tasks. */
  TaskSummaryDto changePriority(UUID taskId, TaskPriority newPriority);
}
