package edu.yacoubi.tasks.services.app.impl;

import edu.yacoubi.tasks.domain.TaskUpdater;
import edu.yacoubi.tasks.domain.dto.request.task.CreateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.UpdateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.UpdateTaskListDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.Task;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import edu.yacoubi.tasks.domain.exception.DomainException;
import edu.yacoubi.tasks.domain.factory.TaskFactory;
import edu.yacoubi.tasks.services.app.ITaskListService;
import edu.yacoubi.tasks.services.app.ITaskListsTaskOrchestrator;
import edu.yacoubi.tasks.services.app.ITaskService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementierung des Orchestrators für Cross-Aggregate-UseCases, die sowohl das TaskList- als auch
 * das Task-Aggregat betreffen.
 *
 * <p>Dieser Service stellt sicher, dass Aggregatsgrenzen nicht verletzt werden und dass fachliche
 * Regeln, die mehrere Aggregates betreffen, zentral implementiert sind.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskListsTaskOrchestratorImpl implements ITaskListsTaskOrchestrator {

  private final ITaskListService taskListService;
  private final ITaskService taskService;
  private final TaskUpdater taskUpdater;

  @Override
  public TaskSummaryDto createTaskInList(final UUID taskListId, final CreateTaskDto dto) {
    log.info("::createTaskInList gestartet mit taskListId={}", taskListId);

    // 1. Aggregat laden
    TaskList taskList = taskListService.getTaskListOrThrow(taskListId);
    log.debug("TaskList geladen: {}", taskList);

    // 2. Domain-Regel prüfen (liegt in der TaskList-Domain)
    taskList.assertCanAddTask();
    log.debug("Domain-Regel geprüft: TaskList {} erlaubt neue Tasks", taskListId);

    // 3. Domain-Objekt erstellen (Factory)
    Task task = TaskFactory.create(dto, taskList);
    log.debug("Task über Factory erstellt: {}", task);

    // 4. Persistieren über TaskService (Persistence-Service)
    TaskSummaryDto created = taskService.createTask(task);
    log.debug("Task gespeichert: {}", created);

    log.info("::createTaskInList erfolgreich abgeschlossen für taskId={} in taskListId={}",
            created.id(), taskListId);

    return created;
  }


  @Override
  public TaskSummaryDto updateTaskInList(
      final UUID taskListId, final UUID taskId, final UpdateTaskDto dto) {

    log.info("::updateTaskInList gestartet mit taskListId={}, taskId={}", taskListId, taskId);

    // 1. TaskList laden
    TaskList taskList = taskListService.getTaskListOrThrow(taskListId);

    if (taskList.isArchived()) {
      throw new IllegalStateException("Archivierte TaskLists können nicht aktualisiert werden.");
    }

    // 2. Task laden
    Task task = taskService.getTaskOrThrow(taskId);

    // 3. Sicherstellen, dass Task zur TaskList gehört
    if (!task.getTaskList().getId().equals(taskListId)) {
      throw new IllegalStateException(
          "Task " + taskId + " gehört nicht zur TaskList " + taskListId);
    }

    // 4. Domain-Update-Methoden aufrufen
    taskUpdater.applyFullUpdate(task, dto);

    // 5. Persistieren + Mapping
    TaskSummaryDto updated = taskService.updateTask(task);

    log.info("::updateTaskInList erfolgreich abgeschlossen für taskId={}", taskId);

    return updated;
  }

  // -----------------------------------------------------------------------------------------
  // ✔ TaskList ist das Aggregate Root
  //   Nur das Root darf entscheiden, ob ein Task entfernt werden darf.
  //
  // ✔ Zugehörigkeit wird im Orchestrator geprüft
  //   Weil es ein Use‑Case‑Schritt ist, keine Domain‑Regel.
  //
  // ✔ Domain führt die eigentliche Operation aus
  //   taskList.removeTask(task) ist die fachliche Aktion.
  //
  // ✔ Persistenz erfolgt über das Root
  //   taskListService.updateTaskList(taskList) ist korrekt.
  //
  // ✔ Orchestrator enthält keine Business‑Logik
  //   Er steuert nur den Ablauf und ruft Domain-Methoden auf.
  // -----------------------------------------------------------------------------------------

  @Override
  public void deleteTaskInList(final UUID taskListId, final UUID taskId) {

    log.debug("Orchestrator: Lösche Task {} in TaskList {}", taskId, taskListId);

    // 1. TaskList laden (Aggregate Root)
    final TaskList taskList = taskListService.getTaskListOrThrow(taskListId);

    // 2. Task laden
    final Task task = taskService.getTaskOrThrow(taskId);

    // 3. Zugehörigkeit prüfen (fachliche Regel)
    if (!taskList.ownsTask(task)) {
      throw new DomainException("Task does not belong to TaskList");
    }

    // 4. Domain-Operation ausführen
    taskList.removeTask(task);

    // 5. Persistieren (Aggregate Root speichern)
    // TODO
    // taskListService.updateTaskList(taskList);

    log.debug("Orchestrator: Task {} in TaskList {} erfolgreich gelöscht", taskId, taskListId);
  }

  // DDD-Konform DONE
  @Override
  @org.springframework.transaction.annotation.Transactional
  public TaskList archiveTaskListIfTasksCompleted(UUID taskListId) {
    log.info(
            "🎯 Orchestrator: 📦 Versuche TaskList {} zu archivieren (nur wenn alle Tasks abgeschlossen sind)",
            taskListId);

    // 1. TaskList laden
    TaskList taskList = taskListService.getTaskListOrThrow(taskListId);

    // 2. Tasks laden
    List<TaskSummaryDto> tasks = taskService.findByTaskListId(taskListId);

    // 3. Prüfen, ob alle Tasks abgeschlossen sind
    boolean allCompleted = tasks.stream().allMatch(t -> t.status() == TaskStatus.COMPLETED);

    if (!allCompleted) {
      log.warn(
              "❌ TaskList {} kann nicht archiviert werden: Es existieren noch offene Tasks",
              taskListId);
      throw new IllegalStateException(
              "TaskList kann nicht archiviert werden, da noch offene Tasks existieren.");
    }

    // 4. Domain-Methode aufrufen
    taskList.archive();

    // 5. Über Aggregat-Service speichern (NICHT Repository!)
    UpdateTaskListDto dto =
            new UpdateTaskListDto(taskList.getTitle(), taskList.getDescription(), taskList.getStatus());

    TaskList archived = taskListService.updateTaskList(taskListId, dto);

    log.info("✅ Orchestrator: TaskList {} erfolgreich archiviert", taskListId);
    return archived;
  }

  @Override
  public boolean isArchivable(UUID taskListId) {
    log.info("🎯 Orchestrator: Prüfe, ob TaskList {} archivierbar ist", taskListId);

    // 1. TaskList existiert?
    taskListService.getTaskListOrThrow(taskListId);

    // 2. Tasks laden
    List<TaskSummaryDto> tasks = taskService.findByTaskListId(taskListId);

    // 3. Regel: Nur archivierbar, wenn alle Tasks abgeschlossen sind
    return tasks.stream().allMatch(t -> t.status() == TaskStatus.COMPLETED);
  }
}
