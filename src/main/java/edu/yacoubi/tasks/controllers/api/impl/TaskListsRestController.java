package edu.yacoubi.tasks.controllers.api;

import edu.yacoubi.tasks.domain.dto.request.task.CreateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.UpdateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.CreateTaskListDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.UpdateTaskListDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.mappers.TaskListMapper;
import edu.yacoubi.tasks.services.app.ITaskListService;
import edu.yacoubi.tasks.services.app.ITaskListsTaskOrchestrator;
import edu.yacoubi.tasks.services.app.ITaskService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskListsRestController implements
        ITaskListsCrudApi,
        ITaskListsScenarioApi,
        ITaskListsTasksApi {

    private final ITaskListService taskListService;
    private final ITaskService taskService;
    private final TaskListMapper taskListMapper;
    private final ITaskListsTaskOrchestrator orchestrator;

    @Override
    public ResponseEntity<APIResponse<List<TaskListDto>>> getAllTaskLists() {
        log.info("📋 Abrufen aller TaskLists");

        // Falls keine TaskLists existieren → EntityNotFoundException wird im Service geworfen
        // und zentral im RestExceptionHandler zu einer 404-Response verarbeitet.
        List<TaskList> taskLists = taskListService.getAllTaskLists();

        log.debug("Gefundene TaskLists: {}", taskLists.size());

        List<TaskListDto> dtos = taskLists.stream()
                .map(taskListMapper::toTaskListDto)
                .toList();

        APIResponse<List<TaskListDto>> response = APIResponse.<List<TaskListDto>>builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("Alle TaskLists erfolgreich abgerufen")
                .data(dtos)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ {} TaskLists erfolgreich abgerufen", dtos.size());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<APIResponse<TaskListDto>> getTaskList(final UUID taskListId) {
        log.info("📥 Abrufen der TaskList mit ID {}", taskListId);

        // Falls TaskList nicht existiert → EntityNotFoundException wird im Service geworfen
        // und zentral im RestExceptionHandler zu einer 404-Response verarbeitet.
        final TaskList taskList = taskListService.getTaskListOrThrow(taskListId);

        log.debug("TaskList gefunden: {}", taskList);

        final TaskListDto dto = taskListMapper.toTaskListDto(taskList);

        final APIResponse<TaskListDto> response = APIResponse.<TaskListDto>builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("TaskList erfolgreich abgerufen")
                .data(dto)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ TaskList mit ID {} erfolgreich abgerufen", taskListId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<APIResponse<TaskListDto>> createTaskList(@Valid CreateTaskListDto dto) {
        log.info("🆕 Erstellen einer neuen TaskList mit Titel: {}", dto.title());

        // Service übernimmt Validierung und Persistenz
        TaskList taskList = taskListService.createTaskList(dto);

        log.debug("Neue TaskList erstellt: {}", taskList);

        TaskListDto taskListDto = taskListMapper.toTaskListDto(taskList);

        APIResponse<TaskListDto> response = APIResponse.<TaskListDto>builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.CREATED.value())
                .message("TaskList erfolgreich erstellt")
                .data(taskListDto)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ TaskList '{}' erfolgreich erstellt mit ID {}", dto.title(), taskList.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<APIResponse<TaskListDto>> updateTaskList(
            final UUID id,
            final UpdateTaskListDto dto) {
        log.info("✏️ Aktualisieren der TaskList mit ID: {}", id);

        // Falls TaskList nicht existiert → EntityNotFoundException wird im Service geworfen
        // und zentral im RestExceptionHandler zu einer 404-Response verarbeitet.
        final TaskList updated = taskListService.updateTaskList(id, dto);

        log.debug("TaskList nach Update: {}", updated);

        final TaskListDto responseDto = taskListMapper.toTaskListDto(updated);

        final APIResponse<TaskListDto> response = APIResponse.<TaskListDto>builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("TaskList erfolgreich aktualisiert")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ TaskList {} erfolgreich aktualisiert", id);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<APIResponse<Void>> deleteTaskList(UUID id) {
        log.info("🗑️ Löschen der TaskList mit ID: {}", id);

        // Falls TaskList nicht existiert → EntityNotFoundException wird im Service geworfen
        // und zentral im RestExceptionHandler zu einer 404-Response verarbeitet.
        taskListService.deleteTaskList(id);

        log.debug("TaskList {} wurde erfolgreich aus der Datenbank entfernt", id);

        APIResponse<Void> response = APIResponse.<Void>builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("TaskList erfolgreich gelöscht")
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ TaskList {} erfolgreich gelöscht", id);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<APIResponse<List<TaskListDto>>> getActiveTaskLists() {
        log.info("📂 Abrufen aller aktiven TaskLists");

        List<TaskList> activeLists = taskListService.getActiveTaskLists();

        log.debug("Gefundene aktive TaskLists: {}", activeLists.size());

        List<TaskListDto> dtos = activeLists.stream()
                .map(taskListMapper::toTaskListDto)
                .toList();

        APIResponse<List<TaskListDto>> response = APIResponse.<List<TaskListDto>>builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("Aktive TaskLists erfolgreich abgerufen")
                .data(dtos)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ {} aktive TaskLists erfolgreich abgerufen", dtos.size());
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<APIResponse<List<TaskListDto>>> getArchivedTaskLists() {
        log.info("📦 Abrufen aller archivierten TaskLists");

        List<TaskList> archivedLists = taskListService.getArchivedTaskLists();

        log.debug("Gefundene archivierte TaskLists: {}", archivedLists.size());

        List<TaskListDto> dtos = archivedLists.stream()
                .map(taskListMapper::toTaskListDto)
                .toList();

        APIResponse<List<TaskListDto>> response = APIResponse.<List<TaskListDto>>builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("Archivierte TaskLists erfolgreich abgerufen")
                .data(dtos)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ {} archivierte TaskLists erfolgreich abgerufen", dtos.size());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<APIResponse<Boolean>> isArchivable(UUID id) {
        log.info("🔎 Prüfen ob TaskList {} archivierbar ist", id);

        boolean archivable = true;//taskListService.isArchivable(id);

        log.debug("Archivierbarkeit für TaskList {}: {}", id, archivable);

        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("Archivierbarkeit erfolgreich geprüft")
                .data(archivable)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ Archivierbarkeit für TaskList {} = {}", id, archivable);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<APIResponse<TaskListDto>> archiveTaskList(UUID id) {
        log.info("📦 Archivieren der TaskList mit ID: {}", id);

        TaskList archived = null;//taskListService.archiveTaskList(id);

        TaskListDto dto = taskListMapper.toTaskListDto(archived);

        APIResponse<TaskListDto> response = APIResponse.<TaskListDto>builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("TaskList erfolgreich archiviert")
                .data(dto)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ TaskList {} erfolgreich archiviert", id);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<APIResponse<List<TaskSummaryDto>>> getTasksByListId(UUID id) {
        log.info("📋 Abrufen aller Tasks für TaskList {}", id);

        // Service liefert bereits TaskSummaryDto → kein zusätzliches Mapping nötig
        List<TaskSummaryDto> tasks = taskService.findByTaskListId(id);

        log.debug("Gefundene Tasks für TaskList {}: {}", id, tasks.size());

        APIResponse<List<TaskSummaryDto>> response = APIResponse.<List<TaskSummaryDto>>builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("Tasks für TaskList erfolgreich abgerufen")
                .data(tasks)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ {} Tasks für TaskList {} erfolgreich abgerufen", tasks.size(), id);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<APIResponse<TaskSummaryDto>> createTaskInList(
            UUID taskListId,
            CreateTaskDto dto) {

        log.info("🆕 Erstelle neuen Task in TaskList {}", taskListId);

        // Orchestrator übernimmt die Koordination zwischen TaskListService & TaskService
        TaskSummaryDto created = orchestrator.createTaskInList(taskListId, dto);

        log.debug("Task nach Erstellung: {}", created);

        APIResponse<TaskSummaryDto> response = APIResponse.<TaskSummaryDto>builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.CREATED.value())
                .message("Task erfolgreich erstellt")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ Task {} erfolgreich in TaskList {} erstellt", created.id(), taskListId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<APIResponse<TaskSummaryDto>> updateTaskInList(
            final UUID taskListId,
            final UUID taskId,
            final UpdateTaskDto dto) {

        log.info("🔄 Update Task {} in TaskList {}", taskId, taskListId);

        TaskSummaryDto updated = orchestrator.updateTaskInList(taskListId, taskId, dto);

        log.debug("Task nach Aktualisierung: {}", updated);

        APIResponse<TaskSummaryDto> response = APIResponse.<TaskSummaryDto>builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("Task erfolgreich aktualisiert")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    // Controller → Orchestrator → Services → Domain → Persistenz → Transformer/Response
    @Override
    public ResponseEntity<APIResponse<Void>> deleteTaskInList(final UUID taskListId, final UUID taskId) {
        log.info("🗑️ Lösche Task {} in TaskList {}", taskId, taskListId);

        orchestrator.deleteTaskInList(taskListId, taskId);

        log.debug("Task {} in TaskList {} erfolgreich gelöscht (Orchestrator abgeschlossen)", taskId, taskListId);

        APIResponse<Void> response = APIResponse.<Void>builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())   // ← HTMX-kompatibel
                .message("Task erfolgreich gelöscht")
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ Task {} in TaskList {} gelöscht", taskId, taskListId);
        return ResponseEntity.ok(response);
    }

}