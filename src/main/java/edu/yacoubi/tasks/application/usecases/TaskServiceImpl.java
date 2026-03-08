package edu.yacoubi.tasks.application.usecases;

import edu.yacoubi.tasks.controllers.api.v1.contract.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.model.Task;
import edu.yacoubi.tasks.infrastructure.mapping.TaskTransformer;
import edu.yacoubi.tasks.infrastructure.mapping.TransformerUtil;
import edu.yacoubi.tasks.infrastructure.persistence.repositories.TaskRepository;
import edu.yacoubi.tasks.application.ports.ITaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ============================================================
 * 🧠 DDD-GEBOTE FÜR DEN TASK-SERVICE
 * ============================================================
 *
 * ✔ TaskService ist ein REINER Persistence-Service
 *   → keine Business-Logik
 *   → keine Domain-Regeln
 *   → keine Status- oder Priority-Änderungen
 *
 * ✔ TaskList ist der Aggregat-Root
 *   → Tasks werden über TaskList erstellt, geändert, gelöscht
 *
 * ✔ TaskService speichert nur Tasks
 *   → Repository.save()
 *   → Repository.findById()
 *
 * ✔ Mapping ist erlaubt
 *   → Task → TaskSummaryDto
 *
 * Dies ist DDD in Reinform.
 * ============================================================
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements ITaskService {

  private final TaskRepository taskRepository;

  @Override
  public Task getTaskOrThrow(UUID taskId) {
    return taskRepository.findById(taskId)
            .orElseThrow(() -> new EntityNotFoundException("Task nicht gefunden: " + taskId));
  }

  @Override
  @Transactional
  public Task save(Task task) {
    return taskRepository.save(task);
  }

  @Override
  public TaskSummaryDto toSummaryDto(Task task) {
    return TransformerUtil.transform(TaskTransformer.TASK_TO_SUMMARY, task);
  }
}
