package edu.yacoubi.tasks.controllers.api.impl;

import edu.yacoubi.tasks.controllers.api.contract.ITaskListsCrudApi;
import edu.yacoubi.tasks.controllers.api.ResponseStatus;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseListTaskListDto;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseTaskListDto;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseVoid;
import edu.yacoubi.tasks.domain.dto.request.tasklist.CreateTaskListDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.PatchTaskListDto;
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
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * ============================================================
 * 🧠 DDD-GEBOTE FÜR DEN TASKLISTS-CRUD-CONTROLLER
 * ============================================================
 *
 * ✔ Der Controller enthält KEINE Business-Logik
 *   → keine Statusregeln
 *   → keine Archivierungsregeln
 *   → keine Task-bezogenen Regeln
 *
 * ✔ Der Controller delegiert ALLE Operationen an den TaskListService
 *   → createTaskList()
 *   → updateTaskList()
 *   → deleteTaskList()
 *   → getTaskListOrThrow()
 *
 * ✔ Der Controller transformiert Domain → DTO
 *   → TaskListTransformer.TASKLIST_TO_DTO
 *   → keine Domain-Manipulation
 *
 * ✔ Der Controller ist zuständig für:
 *   → HTTP-Statuscodes
 *   → API-Response-Wrapper
 *   → Logging
 *   → Validierung der Request-DTOs
 *
 * ✔ Der Controller ist NICHT zuständig für:
 *   → Domain-Regeln
 *   → Persistenz
 *   → Orchestrierung
 *   → Aggregat-Logik
 *
 * ✔ Der Controller ist extrem DÜNN
 *   → 1–2 Zeilen pro Use-Case
 *   → keine Logik, nur Delegation + Response
 *
 * Dies ist DDD in Reinform.
 * ============================================================
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskListsCrudController implements ITaskListsCrudApi {

    private final ITaskListService taskListService;

    @Override
    public ResponseEntity<APIResponseListTaskListDto> getAllTaskLists() {
        log.info("📋 Abrufen aller TaskLists");

        List<TaskList> taskLists = taskListService.getAllTaskLists();
        List<TaskListDto> dtos = taskLists.stream()
                .map(TaskListTransformer.TASKLIST_TO_DTO::transform)
                .toList();

        APIResponseListTaskListDto response = APIResponseListTaskListDto.builder()
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
    public ResponseEntity<APIResponseTaskListDto> getTaskList(UUID id) {
        log.info("📥 Abrufen der TaskList mit ID {}", id);

        TaskList taskList = taskListService.getTaskListOrThrow(id);
        TaskListDto dto = TaskListTransformer.TASKLIST_TO_DTO.transform(taskList);

        APIResponseTaskListDto response = APIResponseTaskListDto.builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("TaskList erfolgreich abgerufen")
                .data(dto)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ TaskList {} erfolgreich abgerufen", id);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<APIResponseTaskListDto> createTaskList(
            @Valid CreateTaskListDto dto
    ) {
        log.info("🆕 Erstelle neue TaskList '{}'", dto.title());

        TaskList taskList = taskListService.createTaskList(dto);
        TaskListDto responseDto = TaskListTransformer.TASKLIST_TO_DTO.transform(taskList);

        APIResponseTaskListDto response = APIResponseTaskListDto.builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.CREATED.value())
                .message("TaskList erfolgreich erstellt")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ TaskList '{}' erfolgreich erstellt (ID={})", dto.title(), taskList.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<APIResponseTaskListDto> updateTaskList(
            UUID id,
            UpdateTaskListDto dto
    ) {
        log.info("✏️ PUT TaskList {}", id);

        TaskList updated = taskListService.updateTaskList(id, dto);
        TaskListDto responseDto = TaskListTransformer.TASKLIST_TO_DTO.transform(updated);

        APIResponseTaskListDto response = APIResponseTaskListDto.builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("TaskList erfolgreich aktualisiert (PUT)")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ TaskList {} erfolgreich aktualisiert (PUT)", id);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<APIResponseTaskListDto> patchTaskList(
            UUID id,
            PatchTaskListDto dto
    ) {
        log.info("🩹 PATCH TaskList {}", id);

        TaskList updated = taskListService.patchTaskList(id, dto);
        TaskListDto responseDto = TaskListTransformer.TASKLIST_TO_DTO.transform(updated);

        APIResponseTaskListDto response = APIResponseTaskListDto.builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("TaskList erfolgreich aktualisiert (PATCH)")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ TaskList {} erfolgreich gepatcht", id);
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<APIResponseVoid> deleteTaskList(UUID id) {
        log.info("🗑️ Lösche TaskList {}", id);

        taskListService.deleteTaskList(id);

        APIResponseVoid response = APIResponseVoid.builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("TaskList erfolgreich gelöscht")
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ TaskList {} erfolgreich gelöscht", id);
        return ResponseEntity.ok(response);
    }
}
