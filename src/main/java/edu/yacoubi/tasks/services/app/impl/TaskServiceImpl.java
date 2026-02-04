package edu.yacoubi.tasks.services.app.impl;

import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.Task;
import edu.yacoubi.tasks.domain.entities.TaskPriority;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import edu.yacoubi.tasks.mappers.TaskMapper;
import edu.yacoubi.tasks.mappers.TaskTransformer;
import edu.yacoubi.tasks.mappers.TransformerUtil;
import edu.yacoubi.tasks.repositories.TaskRepository;
import edu.yacoubi.tasks.services.app.ITaskService;
import edu.yacoubi.tasks.validation.EntityValidator;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements ITaskService {

  private final TaskRepository taskRepository;
  private final TaskMapper mapper;
  private final EntityValidator entityValidator;

  @Override
  public Task getTaskOrThrow(final UUID taskId) {
    // validator.validateTaskExists(taskId); // ✅ technische Validierung
    return taskRepository
        .findById(taskId)
        .orElseThrow(() -> new IllegalArgumentException("Task nicht gefunden: " + taskId));
  }

  @Override
  public List<TaskSummaryDto> findByTaskListId(final UUID taskListId) {
    log.info("::findByTaskListId gestartet mit taskListId={}", taskListId);

    // 1. Existenzprüfung über zentralen Validator
    log.debug("Prüfe Existenz der TaskList mit ID={}", taskListId);
    entityValidator.validateTaskListExists(taskListId);
    log.debug("TaskList {} existiert – lade Tasks", taskListId);

    // 2. Direktes Query-Readmodel laden (DTOs aus Repository)
    List<TaskSummaryDto> tasks = taskRepository.findByTaskListId(taskListId);

    log.info(
        "::findByTaskListId erfolgreich – {} Tasks für TaskList {} gefunden",
        tasks.size(),
        taskListId);

    return tasks;
  }

  @Override
  public List<TaskSummaryDto> findByTaskListIdAndStatus(
      final UUID taskListId, final String status) {
    log.info(
        "::findByTaskListIdAndStatus gestartet mit taskListId={} status={}", taskListId, status);

    TaskStatus parsedStatus = TaskStatus.valueOf(status);

    return taskRepository.findByTaskListIdAndStatus(taskListId, parsedStatus);
  }

  @Override
  public TaskSummaryDto createTask(final Task task) {
    log.info("::createTask gestartet für task={}", task);

    // 1. Technische Validierung
    if (task == null) {
      throw new IllegalArgumentException("Task darf nicht null sein.");
    }

    if (task.getId() != null) {
      throw new IllegalStateException("createTask darf keine bestehende Task aktualisieren.");
    }

    if (task.getTaskList() == null) {
      throw new IllegalStateException("Neue Tasks müssen einer TaskList zugeordnet sein.");
    }

    // 2. Persistieren
    Task saved = taskRepository.save(task);

    log.info("::createTask erfolgreich abgeschlossen für taskId={}", saved.getId());

    // 3. Mapping
    return TransformerUtil.transform(TaskTransformer.TASK_TO_SUMMARY, saved);
  }


  //    @Override
  //    public TaskSummaryDto completeTask(UUID taskId) {
  //        log.info("::completeTask gestartet für taskId={}", taskId);
  //
  //        Task task = getTaskOrThrow(taskId);
  //        task.complete(); // ✅ Domain-Methode
  //
  //        Task saved = taskRepository.save(task);
  //
  //        log.info("::completeTask erfolgreich abgeschlossen für taskId={}", saved.getId());
  //
  //        return mapper.toSummaryDto(saved);
  //    }
  //
  //    @Override
  //    public TaskSummaryDto reopenTask(UUID taskId) {
  //        log.info("::reopenTask gestartet für taskId={}", taskId);
  //
  //        Task task = getTaskOrThrow(taskId);
  //        task.reopen(); // ✅ Domain-Methode
  //
  //        Task saved = taskRepository.save(task);
  //
  //        log.info("::reopenTask erfolgreich abgeschlossen für taskId={}", saved.getId());
  //
  //        return mapper.toSummaryDto(saved);
  //    }

  @Override
  @Transactional
  public TaskSummaryDto updateTask(final Task task) {

    UUID taskId = task.getId();
    log.info("::updateTask gestartet für taskId={}", taskId);

    try {
      // Persistieren
      Task saved = taskRepository.save(task);

      log.debug("Task erfolgreich gespeichert: {}", saved);

      // Mapping
      TaskSummaryDto dto = TransformerUtil.transform(TaskTransformer.TASK_TO_SUMMARY, saved);

      log.info("::updateTask erfolgreich abgeschlossen für taskId={}", taskId);
      return dto;

    } catch (OptimisticLockException e) {
      log.error("Optimistic Locking Fehler beim Aktualisieren von taskId={}", taskId, e);
      throw new IllegalStateException(
          "Die Aufgabe wurde parallel geändert. Bitte erneut versuchen.");
    } catch (Exception e) {
      log.error("Fehler beim Aktualisieren von taskId={}", taskId, e);
      throw new IllegalStateException("Task konnte nicht aktualisiert werden.");
    }
  }

  @Override
  public TaskSummaryDto changePriority(final UUID taskId, final TaskPriority newPriority) {
    log.info("::changePriority gestartet für taskId={} newPriority={}", taskId, newPriority);

    Task task = getTaskOrThrow(taskId);
    task.changePriority(newPriority); // ✅ Domain-Methode

    Task saved = taskRepository.save(task);

    log.info("::changePriority erfolgreich abgeschlossen für taskId={}", saved.getId());

    return mapper.toSummaryDto(saved);
  }
}
