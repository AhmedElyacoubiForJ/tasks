package edu.yacoubi.tasks.controllers.api.impl;

import edu.yacoubi.tasks.controllers.api.APIResponse;
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

@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskListsScenarioController implements ITaskListsScenarioApi {

    private final ITaskListService taskListService;
    private final ITaskListsTaskOrchestrator orchestrator;

    @Override // DDD-Konform DONE
    public ResponseEntity<APIResponseListTaskListDto> getActiveTaskLists() {
        log.info("📂 Abrufen aller aktiven TaskLists");

        List<TaskList> activeLists = taskListService.getActiveTaskLists();

        log.debug("Gefundene aktive TaskLists: {}", activeLists.size());

        List<TaskListDto> dtos = activeLists.stream()
                .map(TaskListTransformer.TASKLIST_TO_DTO::transform)
                .toList();

        APIResponseListTaskListDto response =
                APIResponseListTaskListDto.builder()
                        .status(ResponseStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .message("Aktive TaskLists erfolgreich abgerufen")
                        .data(dtos)
                        .timestamp(LocalDateTime.now())
                        .build();

        log.info("✅ {} aktive TaskLists erfolgreich abgerufen", dtos.size());
        return ResponseEntity.ok(response);
    }

    @Override // DDD-Konform DONE
    public ResponseEntity<APIResponseListTaskListDto> getArchivedTaskLists() {
        log.info("📦 Abrufen aller archivierten TaskLists");

        List<TaskList> archivedLists = taskListService.getArchivedTaskLists();

        log.debug("Gefundene archivierte TaskLists: {}", archivedLists.size());

        List<TaskListDto> dtos = archivedLists.stream()
                .map(TaskListTransformer.TASKLIST_TO_DTO::transform)
                .toList();

        APIResponseListTaskListDto response =
                APIResponseListTaskListDto.builder()
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
    public ResponseEntity<APIResponseTaskListDto> archiveTaskList(final UUID id) {
        log.info("📦 REST: Archivieren der TaskList mit ID {}", id);

        // 1. Orchestrator-UseCase ausführen
        final TaskList archived = orchestrator.archiveTaskList(id);

        // 2. Domain → DTO transformieren (neuer Transformer, kein MapStruct)
        final TaskListDto dto = TaskListTransformer.TASKLIST_TO_DTO.transform(archived);

        // 3. API-Response bauen
        APIResponseTaskListDto response =
                APIResponseTaskListDto.builder()
                        .status(ResponseStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .message("TaskList erfolgreich archiviert")
                        .data(dto)
                        .timestamp(LocalDateTime.now())
                        .build();

        log.info("✅ REST: TaskList {} erfolgreich archiviert", id);
        return ResponseEntity.ok(response);
    }
}
