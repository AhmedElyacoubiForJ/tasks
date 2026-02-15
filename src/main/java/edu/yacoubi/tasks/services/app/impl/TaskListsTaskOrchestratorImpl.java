package edu.yacoubi.tasks.services.app.impl;

import edu.yacoubi.tasks.domain.TaskUpdater;
import edu.yacoubi.tasks.domain.dto.request.task.CreateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.FullUpdateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.PatchTaskDto;
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
import org.springframework.transaction.annotation.Transactional;

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
          final UUID taskListId,
          final UUID taskId,
          final FullUpdateTaskDto dto
  ) {
    log.info("::updateTaskInList gestartet mit taskListId={}, taskId={}", taskListId, taskId);

    // 1. TaskList laden (Aggregat-Root)
    TaskList taskList = taskListService.getTaskListOrThrow(taskListId);

    // 2. Domain-Regel: archivierte Listen dürfen nicht verändert werden
    if (taskList.isArchived()) {
      throw new IllegalStateException("Archivierte TaskLists können nicht aktualisiert werden.");
    }

    // 3. Task laden
    Task task = taskService.getTaskOrThrow(taskId);

    // 4. Sicherstellen, dass Task zur TaskList gehört
    if (!task.getTaskList().getId().equals(taskListId)) {
      throw new IllegalStateException(
              "Task " + taskId + " gehört nicht zur TaskList " + taskListId
      );
    }

    // 5. Domain-Update anwenden (über Updater → ruft Domain-Methoden auf)
    taskUpdater.applyFullUpdate(task, dto);

    // 6. Persistieren + Mapping
    TaskSummaryDto updated = taskService.updateTask(task);

    log.info("::updateTaskInList erfolgreich abgeschlossen für taskId={}", taskId);

    return updated;
  }

  @Override
  public TaskSummaryDto patchTaskInList(
          final UUID taskListId,
          final UUID taskId,
          final PatchTaskDto dto
  ) {
    log.info("::patchTaskInList gestartet mit taskListId={}, taskId={}", taskListId, taskId);

    // 1. TaskList laden (Aggregat-Root)
    TaskList taskList = taskListService.getTaskListOrThrow(taskListId);

    // 2. Domain-Regel: archivierte Listen dürfen nicht verändert werden
    if (taskList.isArchived()) {
      throw new IllegalStateException("Archivierte TaskLists können nicht aktualisiert werden.");
    }

    // 3. Task laden
    Task task = taskService.getTaskOrThrow(taskId);

    // 4. Sicherstellen, dass Task zur TaskList gehört
    if (!task.getTaskList().getId().equals(taskListId)) {
      throw new IllegalStateException(
              "Task " + taskId + " gehört nicht zur TaskList " + taskListId
      );
    }

    // 5. Partielle Änderungen anwenden
    taskUpdater.applyPatch(task, dto);

    // 6. Persistieren + Mapping
    TaskSummaryDto updated = taskService.updateTask(task);

    log.info("::patchTaskInList erfolgreich abgeschlossen für taskId={}", taskId);

    return updated;
  }

  @Override
  public void deleteTaskInList(
          final UUID taskListId,
          final UUID taskId
  ) {

    log.debug("Orchestrator: Lösche Task {} in TaskList {}", taskId, taskListId);

    // 1. TaskList laden (Aggregate Root)
    final TaskList taskList = taskListService.getTaskListOrThrow(taskListId);

    // 2. Task laden
    final Task task = taskService.getTaskOrThrow(taskId);

    // 3. Zugehörigkeit prüfen
    if (!taskList.ownsTask(task)) {
      throw new DomainException("Task does not belong to TaskList");
    }

    // 4. Domain-Operation
    taskList.removeTask(task);

    // 5. Persistieren des Aggregate Roots
    taskListService.save(taskList);

    log.debug("Orchestrator: Task {} in TaskList {} erfolgreich gelöscht", taskId, taskListId);
  }

//  @Override
//  @org.springframework.transaction.annotation.Transactional
//  public TaskList archiveTaskList(final UUID taskListId) {
//    log.info(
//            "🎯 Orchestrator: 📦 Versuche TaskList {} zu archivieren (nur wenn alle Tasks abgeschlossen sind)",
//            taskListId
//    );
//
//    // 1. TaskList laden → ✔️ korrekt
//    // Der Orchestrator darf die Aggregate Root laden.
//    final TaskList taskList = taskListService.getTaskListOrThrow(taskListId);
//
//    // ❌ 2. Tasks laden → DDD-Verstoß
//    // Der Orchestrator darf NICHT Tasks separat laden.
//    // Tasks gehören zum TaskList-Aggregat und müssen über taskList.getTasks() kommen.
//    // Außerdem: TaskService im Orchestrator ist ein Architekturfehler.
//    final List<TaskSummaryDto> tasks = taskService.findByTaskListId(taskListId);
//
//    // ❌ 3. Prüfen, ob alle Tasks abgeschlossen sind → Domain-Logik im Orchestrator
//    // Diese Regel gehört 100% in die Domain (TaskList.isArchivable()).
//    // Der Orchestrator darf KEINE fachlichen Regeln implementieren.
//    final boolean allCompleted = tasks.stream()
//            .allMatch(t -> t.status() == TaskStatus.COMPLETED);
//
//    if (!allCompleted) {
//      // ❌ Orchestrator entscheidet über Business-Regel
//      // Das ist Aufgabe der Domain (taskList.archive() sollte selbst prüfen).
//      log.warn(
//              "❌ TaskList {} kann nicht archiviert werden: Es existieren noch offene Tasks",
//              taskListId);
//      throw new IllegalStateException(
//              "TaskList kann nicht archiviert werden, da noch offene Tasks existieren.");
//    }
//
//    // ✔️ 4. Domain-Methode aufrufen → richtig
//    // ABER: Die Domain-Methode sollte SELBST prüfen, ob archivierbar.
//    // Der Orchestrator sollte NICHT vorher prüfen.
//    taskList.archive();
//
//    // ❌ 5. Über Aggregat-Service speichern (NICHT Repository!) → Idee gut, Umsetzung falsch
//    // ABER: Der Orchestrator darf KEINE DTOs bauen.
//    // UpdateTaskListDto ist ein API-Objekt und hat im Orchestrator nichts verloren.
//    final UpdateTaskListDto dto = new UpdateTaskListDto(
//            taskList.getTitle(),
//            taskList.getDescription(),
//            taskList.getStatus()
//    );
//
//    // ❌ 6. updateTaskList() aufrufen → falscher UseCase
//    // Der Orchestrator soll einfach taskListService.save(taskList) aufrufen.
//    // updateTaskList() ist ein API-UseCase, kein Aggregat-Speichermechanismus.
//    final TaskList archived = taskListService.updateTaskList(taskListId, dto);
//
//    log.info("✅ Orchestrator: TaskList {} erfolgreich archiviert", taskListId);
//    return archived;
//  }

  @Override
  @Transactional
  public TaskList archiveTaskList(UUID taskListId) {
    log.info("🎯 Orchestrator: Archivieren der TaskList {}", taskListId);

    TaskList list = taskListService.getTaskListOrThrow(taskListId);

    // Domain entscheidet, ob archivieren erlaubt ist
    list.archive();

    // Aggregat speichern
    TaskList saved = taskListService.save(list);

    log.info("✅ Orchestrator: TaskList {} erfolgreich archiviert", taskListId);

    return saved;
  }
}
