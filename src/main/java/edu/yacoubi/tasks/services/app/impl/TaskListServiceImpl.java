package edu.yacoubi.tasks.services.app.impl;

import edu.yacoubi.tasks.domain.dto.request.tasklist.CreateTaskListDto;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskListServiceImpl implements ITaskListService {

    private final TaskListRepository taskListRepository;

    @Override
    public List<TaskList> getAllTaskLists() {
        log.info("📋 Service: Abrufen aller TaskLists");

        List<TaskList> taskLists = taskListRepository.findAll();

        log.info("✅ Service: {} TaskLists gefunden", taskLists.size());
        return taskLists;
    }

    @Override
    public List<TaskList> getActiveTaskLists() {
        log.info("📋 Service: Abrufen aller aktiven TaskLists");

        // Gibt eine leere Liste zurück, wenn keine aktiven TaskLists existieren.
        List<TaskList> taskLists = taskListRepository.findByStatus(TaskListStatus.ACTIVE);

        log.info("✅ Service: {} aktive TaskLists gefunden", taskLists.size());
        return taskLists;
    }

    @Override
    public List<TaskList> getArchivedTaskLists() {
        log.info("📋 Service: Abrufen aller archivierten TaskLists");

        // Gibt eine leere Liste zurück, wenn keine archived TaskLists existieren.
        List<TaskList> taskLists = taskListRepository.findByStatus(TaskListStatus.ARCHIVED);

        log.info("✅ Service: {} archivierte TaskLists gefunden", taskLists.size());
        return taskLists;
    }

    @Override
    public Page<TaskListDto> getFilteredTaskLists(final TaskListFilterDto params) {
        final Pageable pageable = PageRequest.of(params.page(), params.size());

        if (params.query() != null && !params.query().isBlank()) {
            return taskListRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    params.query(), params.query(), pageable
            ).map(taskList -> TransformerUtil.transform(TaskListTransformer.TASKLIST_TO_DTO, taskList));
      // .map(TaskListTransformer.TASKLIST_TO_DTO::transform)

    }

        return taskListRepository.findAll(pageable)
                .map(taskList -> TransformerUtil.transform(TaskListTransformer.TASKLIST_TO_DTO, taskList));
    }

    @Override
    public TaskList getTaskListOrThrow(final UUID id) {
        log.info("📥 Lade TaskList mit ID {}", id);

        return taskListRepository.findById(id)
                .orElseThrow(() -> logAndThrowNotFound(id));
    }

    @Override
    public TaskList createTaskList(final CreateTaskListDto dto) {
        log.info("🆕 Service: Erstelle neue TaskList mit Titel '{}'", dto.title());

        // 1. Aggregat erzeugen (Domain-Builder prüft Invarianten)
        TaskList taskList = TaskList.builder()
                .title(dto.title())
                .description(dto.description())
                .build();

        // 2. Persistieren
        TaskList savedTaskList = taskListRepository.save(taskList);

        log.info("✅ Service: TaskList '{}' erfolgreich erstellt mit ID {}",
                savedTaskList.getTitle(), savedTaskList.getId());

        return savedTaskList;
    }

    @Override
    public void deleteTaskList(final UUID id) {
        log.info("🗑️ Service: Versuche TaskList mit ID {} zu löschen", id);

        if (!taskListRepository.existsById(id)) {
            throw logAndThrowNotFound(id);
        }

        taskListRepository.deleteById(id);
        log.info("✅ Service: TaskList mit ID {} erfolgreich gelöscht", id);
    }

    @Override
    public TaskList updateTaskList(
            final UUID id,
            final UpdateTaskListDto dto
    ) {
        log.info("✏️ Service: Aktualisiere TaskList mit ID {}", id);

        // 1. Aggregat laden oder 404
        TaskList taskList = getTaskListOrThrow(id);

        // 2. Logging der Änderungen
        logFieldChange("title", taskList.getTitle(), dto.title());
        logFieldChange("description", taskList.getDescription(), dto.description());

        // 3. Domain-Methoden anwenden (keine Setter!)
        taskList.rename(dto.title());
        taskList.changeDescription(dto.description());

        // 4. Persistieren
        TaskList updatedTaskList = taskListRepository.save(taskList);

        log.info("✅ Service: TaskList {} erfolgreich aktualisiert", id);
        return updatedTaskList;
    }

    @Override
    public TaskList activateTaskList(UUID id) {
        log.info("🔄 Aktiviere TaskList mit ID {}", id);

        TaskList taskList = getTaskListOrThrow(id);

        // Domain-Methode führt Statuswechsel + updated() aus
        taskList.activate();

        TaskList updated = taskListRepository.save(taskList);

        log.info("✅ TaskList {} erfolgreich aktiviert", id);
        return updated;
    }

    @Transactional
    public TaskList save(final TaskList taskList) {
        log.debug("TaskListService: Speichere TaskList {}", taskList.getId());
        return taskListRepository.save(taskList);
    }

    /**
     * Private Hilfsmethode für konsistentes Logging + Exception Handling
     */
    private EntityNotFoundException logAndThrowNotFound(UUID id) {
        log.error("❌ [Service] TaskList mit ID {} nicht gefunden – EntityNotFoundException wird hier geworfen", id);
        return new EntityNotFoundException("TaskList " + id + " wurde nicht gefunden");
    }

    private void logFieldChange(String field, Object oldValue, Object newValue) {
        log.info("📋 Feld '{}' geändert: alt='{}', neu='{}'", field, oldValue, newValue);
        //log.debug("📋 Feld '{}' geändert: alt='{}', neu='{}'", field, oldValue, newValue);
    }
}
