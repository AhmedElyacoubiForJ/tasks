package edu.yacoubi.tasks.controllers.api.v1.impl;

import edu.yacoubi.tasks.controllers.api.v1.contract.responses.ResponseStatus;
import edu.yacoubi.tasks.controllers.api.v1.contract.ITaskListsTasksCrudApi;
import edu.yacoubi.tasks.controllers.api.v1.contract.responses.swagger.APIResponseListTaskSummaryDto;
import edu.yacoubi.tasks.controllers.api.v1.contract.responses.swagger.APIResponseTaskSummaryDto;
import edu.yacoubi.tasks.controllers.api.v1.contract.responses.swagger.APIResponseVoid;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.task.CreateTaskDto;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.task.FullUpdateTaskDto;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.request.task.PatchTaskDto;
import edu.yacoubi.tasks.controllers.api.v1.contract.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.model.TaskList;
import edu.yacoubi.tasks.application.ports.ITaskListService;
import edu.yacoubi.tasks.application.ports.ITaskListsTaskOrchestrator;
import edu.yacoubi.tasks.application.ports.ITaskService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * ============================================================
 * 🧠 DDD-GEBOTE FÜR DEN TASKLISTS–TASKS-CONTROLLER
 * ============================================================
 *
 * ✔ Der Controller enthält KEINE Business-Logik
 *   → keine Statusregeln
 *   → keine Archivierungsregeln
 *   → keine Task- oder TaskList-Regeln
 *
 * ✔ Der Controller delegiert ALLES an den Orchestrator
 *   → orchestrator.createTaskInList(...)
 *   → orchestrator.updateTaskInList(...)
 *   → orchestrator.patchTaskInList(...)
 *   → orchestrator.deleteTaskInList(...)
 *
 * ✔ Der Controller lädt NIEMALS Domain-Objekte direkt
 *   → außer für reine Read-Use-Cases (GET)
 *
 * ✔ Der Controller ist zuständig für:
 *   → HTTP-Statuscodes
 *   → API-Response-Wrapper
 *   → Logging
 *   → Validierung der DTOs (falls nötig)
 *
 * ✔ Der Controller ist NICHT zuständig für:
 *   → Domain-Regeln
 *   → Aggregat-Logik
 *   → Persistenz
 *   → Orchestrierung
 *
 * ✔ Der Controller ist extrem DÜNN
 *   → 1 Zeile pro Use-Case: orchestrator.xyz(...)
 *   → Response bauen
 *
 * Dies ist DDD in Reinform.
 * ============================================================
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskListsTasksCrudController implements ITaskListsTasksCrudApi {

  private final ITaskService taskService;
  private final ITaskListService taskListService;
  private final ITaskListsTaskOrchestrator orchestrator;

  @Override
  public ResponseEntity<APIResponseTaskSummaryDto> patchTaskInList(
          UUID taskListId,
          UUID taskId,
          PatchTaskDto dto
  ) {
    log.info("🔧 PATCH Task {} in TaskList {}", taskId, taskListId);

    TaskSummaryDto updated = orchestrator.patchTaskInList(taskListId, taskId, dto);

    APIResponseTaskSummaryDto response = APIResponseTaskSummaryDto.builder()
            .status(ResponseStatus.SUCCESS)
            .statusCode(HttpStatus.OK.value())
            .message("Task erfolgreich aktualisiert (PATCH)")
            .data(updated)
            .timestamp(LocalDateTime.now())
            .build();

    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<APIResponseListTaskSummaryDto> getTasksByListId(UUID id) {
    log.info("📋 Abrufen aller Tasks für TaskList {}", id);

    TaskList list = taskListService.getTaskListOrThrow(id);

    List<TaskSummaryDto> tasks = list.getTasks().stream()
            .map(taskService::toSummaryDto)
            .toList();

    APIResponseListTaskSummaryDto response = APIResponseListTaskSummaryDto.builder()
            .status(ResponseStatus.SUCCESS)
            .statusCode(HttpStatus.OK.value())
            .message("Tasks für TaskList erfolgreich abgerufen")
            .data(tasks)
            .timestamp(LocalDateTime.now())
            .build();

    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<APIResponseTaskSummaryDto> createTaskInList(
          UUID taskListId,
          CreateTaskDto dto
  ) {
    log.info("🆕 Erstelle neuen Task in TaskList {}", taskListId);

    TaskSummaryDto created = orchestrator.createTaskInList(taskListId, dto);

    APIResponseTaskSummaryDto response = APIResponseTaskSummaryDto.builder()
            .status(ResponseStatus.SUCCESS)
            .statusCode(HttpStatus.CREATED.value())
            .message("Task erfolgreich erstellt")
            .data(created)
            .timestamp(LocalDateTime.now())
            .build();

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<APIResponseTaskSummaryDto> updateTaskInList(
          UUID taskListId,
          UUID taskId,
          FullUpdateTaskDto dto
  ) {
    log.info("🔄 Full Update Task {} in TaskList {}", taskId, taskListId);

    TaskSummaryDto updated = orchestrator.updateTaskInList(taskListId, taskId, dto);

    APIResponseTaskSummaryDto response = APIResponseTaskSummaryDto.builder()
            .status(ResponseStatus.SUCCESS)
            .statusCode(HttpStatus.OK.value())
            .message("Task erfolgreich aktualisiert (Full Update)")
            .data(updated)
            .timestamp(LocalDateTime.now())
            .build();

    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<APIResponseVoid> deleteTaskInList(UUID taskListId, UUID taskId) {
    log.info("🗑️ Lösche Task {} in TaskList {}", taskId, taskListId);

    orchestrator.deleteTaskInList(taskListId, taskId);

    APIResponseVoid response = APIResponseVoid.builder()
            .status(ResponseStatus.SUCCESS)
            .statusCode(HttpStatus.OK.value())
            .message("Task erfolgreich gelöscht")
            .timestamp(LocalDateTime.now())
            .build();

    return ResponseEntity.ok(response);
  }
}
