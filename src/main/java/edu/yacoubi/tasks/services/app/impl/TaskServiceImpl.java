package edu.yacoubi.tasks.services.app.impl;

import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.Task;
import edu.yacoubi.tasks.mappers.TaskTransformer;
import edu.yacoubi.tasks.mappers.TransformerUtil;
import edu.yacoubi.tasks.repositories.TaskRepository;
import edu.yacoubi.tasks.services.app.ITaskService;
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
