package edu.yacoubi.tasks.services.app.impl;

import edu.yacoubi.tasks.domain.TaskUpdater;
import edu.yacoubi.tasks.domain.dto.request.task.CreateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.FullUpdateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.PatchTaskDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.Task;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.domain.entities.TaskPriority;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import edu.yacoubi.tasks.mappers.TaskTransformer;
import edu.yacoubi.tasks.services.app.ITaskListService;
import edu.yacoubi.tasks.services.app.ITaskListsTaskOrchestrator;
import java.util.UUID;

import edu.yacoubi.tasks.services.app.ITaskService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ============================================================
 * 🧠 DDD-GEBOTE FÜR DEN TASKLISTS–TASK–ORCHESTRATOR
 * ============================================================
 *
 * ✔ Der Orchestrator ist der EINZIGE Ort für Use-Cases,
 *   die mehrere Aggregate betreffen
 *   → z. B. "Task in TaskList erstellen"
 *   → z. B. "Task in TaskList aktualisieren"
 *   → z. B. "Task in TaskList löschen"
 *   → z. B. "TaskList archivieren"
 *
 * ✔ Der Orchestrator enthält KEINE fachliche Logik
 *   → keine Statusregeln
 *   → keine Archivierungsregeln
 *   → keine Task- oder TaskList-Regeln
 *   → keine Validierungen außer Existenzprüfungen
 *
 * ✔ Der Orchestrator ruft ausschließlich Domain-Methoden auf
 *   → taskList.createTask(...)
 *   → taskUpdater.applyFullUpdate(...)
 *   → taskUpdater.applyPatch(...)
 *   → taskList.removeTask(...)
 *   → taskList.archive()
 *
 * ✔ Der Orchestrator entscheidet NICHT über Business-Regeln
 *   → Domain schützt ihre eigenen Invarianten
 *   → Domain entscheidet über Statuswechsel
 *   → Domain entscheidet über Archivierung
 *
 * ✔ Der Orchestrator lädt Aggregate über Services,
 *   aber manipuliert sie niemals direkt
 *   → TaskListService.getTaskListOrThrow(...)
 *   → TaskListService.save(...)
 *
 * ✔ Der Orchestrator ist TRANSACTIONAL
 *   → garantiert atomare Use-Cases
 *   → Domain-Methoden + Persistenz = eine Einheit
 *
 * ✔ Der Orchestrator ist extrem DÜNN
 *   → keine Mapping-Logik außer DTO-Ausgabe
 *   → keine Repository-Zugriffe
 *   → keine Setter
 *   → keine Aggregat-Regeln
 *
 * ✔ Der Orchestrator ist der "Application Layer"
 *   → koordiniert Use-Cases
 *   → verbindet Domain + Persistence + DTOs
 *   → aber enthält selbst KEINE Logik
 *
 * Dies ist DDD in Reinform.
 * ============================================================
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskListsTaskOrchestratorImpl
        implements ITaskListsTaskOrchestrator {

  private final ITaskListService taskListService;
  private final ITaskService taskService;
  private final TaskUpdater taskUpdater;

  @Override
  @Transactional
  public TaskSummaryDto createTaskInList(
          final UUID taskListId,
          final CreateTaskDto dto
  ) {
    log.info("::createTaskInList gestartet mit taskListId={}", taskListId);

    TaskList taskList = taskListService.getTaskListOrThrow(taskListId);

    // Domain erstellt Task und gibt das Objekt zurück
    Task task = taskList.createTask(
            dto.title(),
            dto.description(),
            dto.dueDate(),
            dto.priority() != null ? dto.priority() : TaskPriority.MEDIUM
    );

    // Aggregat speichern (Task wird per Cascade mitgespeichert)
    taskListService.save(taskList); // flush inside

    TaskSummaryDto created = TaskTransformer.TASK_TO_SUMMARY.transform(task);

    // TODO bidirectional id setting don't working
    log.info("::createTaskInList erfolgreich abgeschlossen für taskId={} in taskListId={}",
            created.id(), taskListId); // created.id() is immer noch null

    return created;
  }

  @Override
  @Transactional
  public TaskSummaryDto updateTaskInList(
          final UUID taskListId,
          final UUID taskId,
          final FullUpdateTaskDto dto
  ) {
    log.info("::updateTaskInList gestartet mit taskListId={}, taskId={}", taskListId, taskId);

    TaskList taskList = taskListService.getTaskListOrThrow(taskListId);

    // Task aus dem Aggregat holen (nicht separat über TaskService)
    Task task = taskList.getTasks().stream()
            .filter(t -> t.getId().equals(taskId))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                    "Task " + taskId + " gehört nicht zur TaskList " + taskListId
            ));

    // Domain-Update (ruft Domain-Methoden auf)
    taskUpdater.applyFullUpdate(task, dto);

    // Aggregat speichern
    taskListService.save(taskList);

    TaskSummaryDto updated = TaskTransformer.TASK_TO_SUMMARY.transform(task);

    log.info("::updateTaskInList erfolgreich abgeschlossen für taskId={}", taskId);

    return updated;
  }

  @Override
  @Transactional
  public TaskSummaryDto patchTaskInList(
          final UUID taskListId,
          final UUID taskId,
          final PatchTaskDto dto
  ) {
    log.info("::patchTaskInList gestartet mit taskListId={}, taskId={}", taskListId, taskId);

    TaskList taskList = taskListService.getTaskListOrThrow(taskListId);

    Task task = taskList.getTasks().stream()
            .filter(t -> t.getId().equals(taskId))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                    "Task " + taskId + " gehört nicht zur TaskList " + taskListId
            ));

    taskUpdater.applyPatch(task, dto);

    taskListService.save(taskList);

    TaskSummaryDto updated = TaskTransformer.TASK_TO_SUMMARY.transform(task);

    log.info("::patchTaskInList erfolgreich abgeschlossen für taskId={}", taskId);

    return updated;
  }

  @Override
  @Transactional
  public void deleteTaskInList(
          final UUID taskListId,
          final UUID taskId
  ) {
    log.debug("::deleteTaskInList Lösche Task {} in TaskList {}", taskId, taskListId);

    TaskList taskList = taskListService.getTaskListOrThrow(taskListId);

    // Orchestrator greift nichts direkt auf entities über Repo. sondern über services
    Task task = taskService.getTaskOrThrow(taskId);

    // DDD-Nicht-Konform
    //    Task task = taskList.getTasks().stream()
//            .filter(t -> t.getId().equals(taskId))
//            .findFirst()
//            .orElseThrow(() -> new IllegalStateException(
//                    "Task " + taskId + " gehört nicht zur TaskList " + taskListId
//            ));

    taskList.removeTask(task);

    taskListService.save(taskList);

    log.debug("::deleteTaskInList Task {} in TaskList {} erfolgreich gelöscht", taskId, taskListId);
  }

  @Override
  @Transactional
  public TaskList archiveTaskList(
          final UUID taskListId
  ) {
    log.info("::archiveTaskList Archivieren der TaskList {}", taskListId);

    TaskList list = taskListService.getTaskListOrThrow(taskListId);

    list.archive();

    TaskList saved = taskListService.save(list);

    log.info("::archiveTaskList TaskList {} erfolgreich archiviert", taskListId);

    return saved;
  }

  @Override
  public TaskList changeTaskStatus(
          final UUID taskListId,
          final UUID taskId,
          final TaskStatus taskStatus
  ) {
    log.info(
            "::changeTaskStatus gestartet für taskListId={} taskId={} status={}",
            taskListId,
            taskId,
            taskStatus
    );

    TaskList taskList = taskListService.getTaskListOrThrow(taskListId);

    // 👉 Domain-Methode auf dem Aggregat-Root
    taskList.changeTaskStatus(taskId, taskStatus);

    // Orchestrator gereift nichts direkt auf repos
    TaskList updatedTaskList = taskListService.save(taskList);

    log.info(
            "::changeTaskStatus erfolgreich abgeschlossen für taskListId={} taskId={}",
            taskListId,
            taskId
    );

    return updatedTaskList;
  }

}
