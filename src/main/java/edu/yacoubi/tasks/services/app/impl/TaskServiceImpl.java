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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements ITaskService {

  private final TaskRepository taskRepository;
  private final TaskMapper mapper;
  private final EntityValidator validator;

  @Override
  public Task getTaskOrThrow(UUID taskId) {
    // validator.validateTaskExists(taskId); // ✅ technische Validierung
    return taskRepository
        .findById(taskId)
        .orElseThrow(() -> new IllegalArgumentException("Task nicht gefunden: " + taskId));
  }

  @Override
  public List<TaskSummaryDto> findByTaskListId(UUID taskListId) {
    log.info("::findByTaskListId gestartet mit taskListId={}", taskListId);

    // ✅ TaskList-Existenzprüfung findet im Orchestrator statt
    // ✅ TaskService prüft nur Task-bezogene Dinge
    return taskRepository.findByTaskListId(taskListId);
  }

  @Override
  public List<TaskSummaryDto> findByTaskListIdAndStatus(UUID taskListId, String status) {
    log.info(
        "::findByTaskListIdAndStatus gestartet mit taskListId={} status={}", taskListId, status);

    TaskStatus parsedStatus = TaskStatus.valueOf(status);

    return taskRepository.findByTaskListIdAndStatus(taskListId, parsedStatus);
  }

  @Override
  public TaskSummaryDto createTask(Task task) {
    log.info("::createTask gestartet für task={}", task);

    if (task == null) {
      throw new IllegalArgumentException("Task darf nicht null sein.");
    }

    if (task.getId() != null) {
      throw new IllegalStateException("createTask darf keine bestehende Task aktualisieren.");
    }

    // ✅ Keine ID gesetzt → JPA führt garantiert ein INSERT aus
    Task saved = taskRepository.save(task);

    log.info("::createTask erfolgreich abgeschlossen für taskId={}", saved.getId());

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
