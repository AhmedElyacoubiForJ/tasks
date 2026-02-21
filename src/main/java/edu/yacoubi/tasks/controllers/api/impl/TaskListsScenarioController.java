package edu.yacoubi.tasks.controllers.api.impl;

import edu.yacoubi.tasks.controllers.api.contract.ITaskListsScenarioApi;
import edu.yacoubi.tasks.controllers.api.ResponseStatus;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseListTaskListDto;
import edu.yacoubi.tasks.controllers.api.wrappers.APIResponseTaskListDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.mappers.TaskListTransformer;
import edu.yacoubi.tasks.services.app.ITaskListService;
import edu.yacoubi.tasks.services.app.ITaskListsTaskOrchestrator;
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
 * 🧠 DDD-GEBOTE FÜR DEN TASKLISTS-SCENARIO-CONTROLLER
 * ============================================================
 *
 * ✔ Der Controller enthält KEINE Business-Logik
 *   → keine Archivierungsregeln
 *   → keine Statusregeln
 *   → keine Task-bezogenen Regeln
 *
 * ✔ Der Controller delegiert ALLE Use-Cases an:
 *   → TaskListService (reine Queries)
 *   → TaskListsTaskOrchestrator (Use-Cases wie Archivieren)
 *
 * ✔ Der Controller transformiert Domain → DTO
 *   → TaskListTransformer.TASKLIST_TO_DTO
 *   → keine direkte Domain-Manipulation
 *
 * ✔ Der Controller ist zuständig für:
 *   → HTTP-Statuscodes
 *   → API-Response-Wrapper
 *   → Logging
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
public class TaskListsScenarioController implements ITaskListsScenarioApi {

    private final ITaskListService taskListService;
    private final ITaskListsTaskOrchestrator orchestrator;

    @Override
    public ResponseEntity<APIResponseListTaskListDto> getActiveTaskLists() {
        log.info("📂 Abrufen aller aktiven TaskLists");

        List<TaskList> activeLists = taskListService.getActiveTaskLists();
        List<TaskListDto> dtos = activeLists.stream()
                .map(TaskListTransformer.TASKLIST_TO_DTO::transform)
                .toList();

        APIResponseListTaskListDto response = APIResponseListTaskListDto.builder()
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
    public ResponseEntity<APIResponseListTaskListDto> getArchivedTaskLists() {
        log.info("📦 Abrufen aller archivierten TaskLists");

        List<TaskList> archivedLists = taskListService.getArchivedTaskLists();
        List<TaskListDto> dtos = archivedLists.stream()
                .map(TaskListTransformer.TASKLIST_TO_DTO::transform)
                .toList();

        APIResponseListTaskListDto response = APIResponseListTaskListDto.builder()
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
    public ResponseEntity<APIResponseTaskListDto> archiveTaskList(UUID id) {
        log.info("📦 Archivieren der TaskList {}", id);

        TaskList archived = orchestrator.archiveTaskList(id);
        TaskListDto dto = TaskListTransformer.TASKLIST_TO_DTO.transform(archived);

        APIResponseTaskListDto response = APIResponseTaskListDto.builder()
                .status(ResponseStatus.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .message("TaskList erfolgreich archiviert")
                .data(dto)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("✅ TaskList {} erfolgreich archiviert", id);
        return ResponseEntity.ok(response);
    }
}
