package edu.yacoubi.tasks.controllers.api.impl;

import edu.yacoubi.tasks.controllers.api.*;
import edu.yacoubi.tasks.domain.dto.request.task.CreateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.FullUpdateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.PatchTaskDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;

import edu.yacoubi.tasks.services.app.ITaskListsTaskOrchestrator;
import edu.yacoubi.tasks.services.app.ITaskService;
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
public class TaskListsTasksController implements ITaskListsTasksApi {

  private final ITaskService taskService;
  private final ITaskListsTaskOrchestrator orchestrator;


  @Override // 🎉 PATCH UPDATE ENDPOINT = DONE
  public ResponseEntity<APIResponse<TaskSummaryDto>> patchTaskInList(
          final UUID taskListId,
          final UUID taskId,
          final PatchTaskDto dto
  ) {
    log.info("🔧 PATCH Task {} in TaskList {}", taskId, taskListId);

    TaskSummaryDto updated =
            orchestrator.patchTaskInList(taskListId, taskId, dto);

    APIResponse<TaskSummaryDto> response =
            APIResponse.<TaskSummaryDto>builder()
                    .status(ResponseStatus.SUCCESS)
                    .statusCode(HttpStatus.OK.value())
                    .message("Task erfolgreich aktualisiert (PATCH)")
                    .data(updated)
                    .timestamp(LocalDateTime.now())
                    .build();

    return ResponseEntity.ok(response);
  }

  @Override // 🎉 GET /tasklists/{taskListId}/tasks — End‑to‑End Status: DDD-Konform
  public ResponseEntity<APIResponse<List<TaskSummaryDto>>> getTasksByListId(final UUID id) {
    log.info("📋 Abrufen aller Tasks für TaskList {}", id);

    // Service liefert bereits TaskSummaryDto → kein zusätzliches Mapping nötig
    List<TaskSummaryDto> tasks = taskService.findByTaskListId(id);

    log.debug("Gefundene Tasks für TaskList {}: {}", id, tasks.size());

    APIResponse<List<TaskSummaryDto>> response =
            APIResponse.<List<TaskSummaryDto>>builder()
                    .status(ResponseStatus.SUCCESS)
                    .statusCode(HttpStatus.OK.value())
                    .message("Tasks für TaskList erfolgreich abgerufen")
                    .data(tasks)
                    .timestamp(LocalDateTime.now())
                    .build();

    log.info("✅ {} Tasks für TaskList {} erfolgreich abgerufen", tasks.size(), id);
    return ResponseEntity.ok(response);
  }

  @Override // 🎉 POST /tasklists/{taskListId} — End‑to‑End Status: DDD-Konform
  public ResponseEntity<APIResponse<TaskSummaryDto>> createTaskInList(
          final UUID taskListId,
          final CreateTaskDto dto
  ) {
    log.info("🆕 Erstelle neuen Task in TaskList {}", taskListId);

    // Delegation an den Orchestrator (Use-Case)
    TaskSummaryDto created = orchestrator.createTaskInList(taskListId, dto);

    log.debug("Task nach Erstellung: {}", created);

    APIResponse<TaskSummaryDto> response =
            APIResponse.<TaskSummaryDto>builder()
                    .status(ResponseStatus.SUCCESS)
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Task erfolgreich erstellt")
                    .data(created)
                    .timestamp(LocalDateTime.now())
                    .build();

    log.info("✅ Task {} erfolgreich in TaskList {} erstellt", created.id(), taskListId);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override // 🎉 PUT /tasklists/{taskListId} — End‑to‑End Status: DDD-Konform
  public ResponseEntity<APIResponse<TaskSummaryDto>> updateTaskInList(
          final UUID taskListId,
          final UUID taskId,
          final FullUpdateTaskDto dto
  ) {

    log.info("🔄 Full Update Task {} in TaskList {}", taskId, taskListId);

    // Delegation an den Orchestrator (Use-Case)
    TaskSummaryDto updated = orchestrator.updateTaskInList(taskListId, taskId, dto);

    log.debug("Task nach Full-Update: {}", updated);

    APIResponse<TaskSummaryDto> response =
            APIResponse.<TaskSummaryDto>builder()
                    .status(ResponseStatus.SUCCESS)
                    .statusCode(HttpStatus.OK.value())
                    .message("Task erfolgreich aktualisiert (Full Update)")
                    .data(updated)
                    .timestamp(LocalDateTime.now())
                    .build();

    return ResponseEntity.ok(response);
  }

  // Controller → Orchestrator → Services → Domain → Persistenz → Transformer/Response
  @Override // DDD-Konform DONE
  public ResponseEntity<APIResponse<Void>> deleteTaskInList(
          final UUID taskListId,
          final UUID taskId
  ) {
    log.info("🗑️ Lösche Task {} in TaskList {}", taskId, taskListId);

    orchestrator.deleteTaskInList(taskListId, taskId);

    log.debug(
            "Task {} in TaskList {} erfolgreich gelöscht (Orchestrator abgeschlossen)",
            taskId,
            taskListId
    );

    APIResponse<Void> response =
            APIResponse.<Void>builder()
                    .status(ResponseStatus.SUCCESS)
                    .statusCode(HttpStatus.OK.value()) // ← HTMX-kompatibel
                    .message("Task erfolgreich gelöscht")
                    .timestamp(LocalDateTime.now())
                    .build();

    log.info("✅ Task {} in TaskList {} gelöscht", taskId, taskListId);
    return ResponseEntity.ok(response);
  }
}
