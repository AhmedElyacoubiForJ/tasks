package edu.yacoubi.tasks.controllers.api.impl;

import edu.yacoubi.tasks.controllers.api.contract.ITaskListsCrudApi;
import edu.yacoubi.tasks.controllers.api.ResponseStatus;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseListTaskListDto;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseTaskListDto;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseVoid;
import edu.yacoubi.tasks.domain.dto.request.tasklist.CreateTaskListDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.UpdateTaskListDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.mappers.TaskListTransformer;
import edu.yacoubi.tasks.services.app.ITaskListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskListsCrudController implements ITaskListsCrudApi {

    private final ITaskListService taskListService;

    @Override // 🎉 GET /tasklists — End‑to‑End Status: DDD-Konform
    public ResponseEntity<APIResponseListTaskListDto> getAllTaskLists() {
        log.info("📋 Abrufen aller TaskLists");

        List<TaskList> taskLists = taskListService.getAllTaskLists();

        log.debug("Gefundene TaskLists: {}", taskLists.size());
        List<TaskListDto> dtos = taskLists.stream()
                .map(TaskListTransformer.TASKLIST_TO_DTO::transform)
                .toList();

        APIResponseListTaskListDto response =
                APIResponseListTaskListDto.builder()
                        .status(ResponseStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .message("Alle TaskLists erfolgreich abgerufen")
                        .data(dtos)
                        .timestamp(LocalDateTime.now())
                        .build();

        log.info("✅ {} TaskLists erfolgreich abgerufen", dtos.size());
        return ResponseEntity.ok(response);
    }

    @Override // 🎉 GET /tasklists/{id} — End‑to‑End Status: DDD-Konform
    public ResponseEntity<APIResponseTaskListDto> getTaskList(final @PathVariable("id") UUID id) {
        log.info("📥 REST: Abrufen der TaskList mit ID {}", id);

        // 1. TaskList laden (wirft EntityNotFoundException → handled by RestExceptionHandler)
        final TaskList taskList = taskListService.getTaskListOrThrow(id);

        log.debug("REST: TaskList gefunden: {}", taskList);

        // 2. Domain → DTO transformieren (NEUER Transformer, kein MapStruct)
        final TaskListDto dto = TaskListTransformer.TASKLIST_TO_DTO.transform(taskList);

        // 3. API-Response bauen
        final APIResponseTaskListDto response =
                APIResponseTaskListDto.builder()
                        .status(ResponseStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .message("TaskList erfolgreich abgerufen")
                        .data(dto)
                        .timestamp(LocalDateTime.now())
                        .build();

        log.info("✅ REST: TaskList {} erfolgreich abgerufen", id);
        return ResponseEntity.ok(response);
    }

    @Override // 🎉 POST /tasklists — End‑to‑End Status: DDD-Konform
    public ResponseEntity<APIResponseTaskListDto> createTaskList(
            @Valid @RequestBody CreateTaskListDto dto
    ) {
        log.info("🆕 REST: Erstellen einer neuen TaskList mit Titel: {}", dto.title());

        // 1. Service übernimmt Validierung + Persistenz
        TaskList taskList = taskListService.createTaskList(dto);

        log.debug("REST: Neue TaskList erstellt: {}", taskList);

        // 2. Domain → DTO über Transformer (kein alter Mapper mehr)
        TaskListDto taskListDto = TaskListTransformer.TASKLIST_TO_DTO.transform(taskList);

        // 3. API-Response bauen
        APIResponseTaskListDto response =
                APIResponseTaskListDto.builder()
                        .status(ResponseStatus.SUCCESS)
                        .statusCode(HttpStatus.CREATED.value())
                        .message("TaskList erfolgreich erstellt")
                        .data(taskListDto)
                        .timestamp(LocalDateTime.now())
                        .build();

        log.info("✅ REST: TaskList '{}' erfolgreich erstellt mit ID {}", dto.title(), taskList.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override // 🎉 PUT /tasklists/{id} — End‑to‑End Status: DDD-Konform
    public ResponseEntity<APIResponseTaskListDto> updateTaskList(
            final UUID id,
            final UpdateTaskListDto dto
    ) {
        log.info("✏️ REST: Aktualisieren der TaskList mit ID {}", id);

        // 1. Service übernimmt Validierung, Domain-Methoden und Persistenz
        final TaskList updated = taskListService.updateTaskList(id, dto);

        log.debug("REST: TaskList nach Update: {}", updated);

        // 2. Domain → DTO über Transformer (kein alter Mapper mehr)
        final TaskListDto responseDto =
                TaskListTransformer.TASKLIST_TO_DTO.transform(updated);

        // 3. API-Response bauen
        final APIResponseTaskListDto response =
                APIResponseTaskListDto.builder()
                        .status(ResponseStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .message("TaskList erfolgreich aktualisiert")
                        .data(responseDto)
                        .timestamp(LocalDateTime.now())
                        .build();

        log.info("✅ REST: TaskList {} erfolgreich aktualisiert", id);
        return ResponseEntity.ok(response);
    }

    @Override // 🎉 DELETE /tasklists/{id} — End‑to‑End Status: DDD-Konform
    public ResponseEntity<APIResponseVoid> deleteTaskList(final UUID id)
    {
        log.info("🗑️ Löschen der TaskList mit ID: {}", id);

        // Falls TaskList nicht existiert → EntityNotFoundException wird im Service geworfen
        // und zentral im RestExceptionHandler zu einer 404-Response verarbeitet.
        taskListService.deleteTaskList(id);

        log.debug("TaskList {} wurde erfolgreich aus der Datenbank entfernt", id);

        APIResponseVoid response =
                APIResponseVoid.builder()
                        .status(ResponseStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .message("TaskList erfolgreich gelöscht")
                        .timestamp(LocalDateTime.now())
                        .build();

        log.info("✅ TaskList {} erfolgreich gelöscht", id);
        return ResponseEntity.ok(response);
    }
}
