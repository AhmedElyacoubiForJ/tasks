package edu.yacoubi.tasks.services.app.impl;

import edu.yacoubi.tasks.domain.TaskListUpdater;
import edu.yacoubi.tasks.domain.dto.request.tasklist.CreateTaskListDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.PatchTaskListDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.TaskListFilterDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.UpdateTaskListDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.domain.entities.TaskListStatus;
import edu.yacoubi.tasks.mappers.TaskListTransformer;
import edu.yacoubi.tasks.mappers.TransformerUtil;
import edu.yacoubi.tasks.repositories.TaskListRepository;
import edu.yacoubi.tasks.services.app.ITaskListService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ============================================================ 🧠 DDD-GEBOTE FÜR DEN
 * TASKLIST-SERVICE ============================================================
 *
 * <p>✔ Der TaskListService ist ein REINER Persistence-Service → keine Business-Logik → keine
 * Statusregeln → keine Archivierungsregeln → keine Task-bezogenen Regeln
 *
 * <p>✔ Die TaskList ist der Aggregat-Root → TaskListService lädt und speichert NUR TaskLists →
 * Tasks werden niemals direkt über diesen Service manipuliert
 *
 * <p>✔ Domain-Methoden werden IMMER genutzt → rename(), changeDescription(), activate() → keine
 * Setter, keine direkte Feldmanipulation
 *
 * <p>✔ Der Service ist NICHT für Use-Cases zuständig → keine Orchestrierung → keine fachlichen
 * Entscheidungen → keine Validierungen außer Existenzprüfungen
 *
 * <p>✔ Der Orchestrator ist der einzige Ort für Use-Cases → TaskListService stellt nur technische
 * Operationen bereit → Orchestrator ruft Domain-Methoden auf und speichert über diesen Service
 *
 * <p>✔ Der Service kapselt Repository-Zugriffe → find, save, delete, filter → keine Aggregat-Logik
 *
 * <p>✔ Der Service ist TRANSPARENT und DÜNN → Domain macht Regeln → Orchestrator macht Use-Cases →
 * Service macht Persistenz
 *
 * <p>Dies ist DDD in Reinform. ============================================================
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskListServiceImpl implements ITaskListService {

  private final TaskListRepository taskListRepository;
  private final TaskListUpdater updater;

  @Override
  public List<TaskList> getAllTaskLists() {
    log.info("::getAllTaskLists Abrufen aller TaskLists");
    return taskListRepository.findAll();
  }

  @Override
  public List<TaskList> getActiveTaskLists() {
    log.info("::getActiveTaskLists Abrufen aller aktiven TaskLists");
    return taskListRepository.findByStatus(TaskListStatus.ACTIVE);
  }

  @Override
  public List<TaskList> getArchivedTaskLists() {
    log.info("::getArchivedTaskLists Abrufen aller archivierten TaskLists");
    return taskListRepository.findByStatus(TaskListStatus.ARCHIVED);
  }

  @Override
  public Page<TaskListDto> getFilteredTaskLists(TaskListFilterDto params) {
    Pageable pageable = PageRequest.of(params.page(), params.size());

    if (params.query() != null && !params.query().isBlank()) {
      return taskListRepository
          .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
              params.query(), params.query(), pageable)
          .map(
              taskList -> TransformerUtil.transform(TaskListTransformer.TASKLIST_TO_DTO, taskList));
    }

    return taskListRepository
        .findAll(pageable)
        .map(taskList -> TransformerUtil.transform(TaskListTransformer.TASKLIST_TO_DTO, taskList));
  }

  @Override
  public TaskList getTaskListOrThrow(UUID id) {
    return taskListRepository.findById(id).orElseThrow(() -> logAndThrowNotFound(id));
  }

  @Override
  public TaskList createTaskList(CreateTaskListDto dto) {
    log.info("🆕 Erstelle neue TaskList mit Titel '{}'", dto.title());

    TaskList taskList =
        TaskList.builder().title(dto.title()).description(dto.description()).build();

    return taskListRepository.save(taskList);
  }

  @Override
  public void deleteTaskList(UUID id) {
    if (!taskListRepository.existsById(id)) {
      throw logAndThrowNotFound(id);
    }
    taskListRepository.deleteById(id);
  }

  // logFieldChange("title", taskList.getTitle(), dto.title());
  // logFieldChange("description", taskList.getDescription(), dto.description());
  @Override
  public TaskList updateTaskList(UUID id, UpdateTaskListDto dto) {
    log.info("🔄 Full Update TaskList {}", id);

    TaskList list = getTaskListOrThrow(id);

    updater.applyFullUpdate(list, dto);

    return taskListRepository.save(list);
  }

  @Override
  public TaskList patchTaskList(UUID id, PatchTaskListDto dto) {
    log.info("🩹 Patch TaskList {}", id);

    TaskList list = getTaskListOrThrow(id);

    updater.applyPatch(list, dto);

    return taskListRepository.save(list);
  }

  @Override
  public TaskList activateTaskList(UUID id) {
    TaskList taskList = getTaskListOrThrow(id);

    taskList.activate();

    return taskListRepository.save(taskList);
  }

  @Transactional
  public TaskList save(TaskList taskList) {
    return taskListRepository.save(taskList);
  }

  private EntityNotFoundException logAndThrowNotFound(UUID id) {
    log.error("❌ TaskList {} nicht gefunden", id);
    return new EntityNotFoundException("TaskList " + id + " wurde nicht gefunden");
  }

  private void logFieldChange(String field, Object oldValue, Object newValue) {
    log.info("📋 Feld '{}' geändert: alt='{}', neu='{}'", field, oldValue, newValue);
  }
}
