package edu.yacoubi.tasks.services.init;

import edu.yacoubi.tasks.domain.dto.request.tasklist.CreateTaskListDto;
import edu.yacoubi.tasks.domain.entities.Task;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.domain.entities.TaskPriority;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import edu.yacoubi.tasks.services.app.ITaskListService;
import edu.yacoubi.tasks.services.app.ITaskService;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializerService {

  private final ITaskListService taskListService;
  private final ITaskService taskService;

  private final Random random = new Random();
  private final LocalDateTime now = LocalDateTime.now();

  @PostConstruct
  public void init() {
    log.info("🚀 Initialisiere Demo-Daten (DDD-konform)");

    if (!taskListService.getAllTaskLists().isEmpty()) {
      log.info("📦 Datenbank enthält bereits TaskLists – Initialisierung wird übersprungen");
      return;
    }

    // 6 Demo-Listen erzeugen
    for (int i = 1; i <= 6; i++) {

      TaskList list =
          taskListService.createTaskList(
              new CreateTaskListDto("📋 TaskList #" + i, "Auto-generierte Liste für Demo-Zwecke"));

      // Liste 3 und 6 bleiben leer
      if (i == 3 || i == 6) {
        continue;
      }

      // Sonst 3 Tasks erzeugen
      createRandomTask("Aufgabe " + i + ".1", list, true);
      createRandomTask("Aufgabe " + i + ".2", list, false);
      createRandomTask("Aufgabe " + i + ".3", list, random.nextBoolean());
    }

    // Eine vollständige Liste erzeugen
    createCompleteList();

    log.info("✅ Demo-Daten erfolgreich gespeichert");
  }

  private void createRandomTask(String title, TaskList list, boolean withDueDate) {

    Task task =
        Task.builder()
            .title(title)
            .description("Generierter Task: " + title)
            .priority(randomEnum(TaskPriority.class))
            .dueDate(withDueDate ? now.plusDays(random.nextInt(14) + 1) : null)
            .taskList(list)
            .build();

    // Status nachträglich setzen (DDD-konform)
    task.changeStatus(randomEnum(TaskStatus.class));

    taskService.createTask(task);
  }

  private void createCompleteList() {

    TaskList list =
        taskListService.createTaskList(
            new CreateTaskListDto(
                "📋 TaskList #7 - 100% erledigt", "Alle Aufgaben sind abgeschlossen"));

    Task t1 =
        Task.builder()
            .title("Dokumentation finalisieren")
            .description("Task wurde abgeschlossen")
            .priority(TaskPriority.MEDIUM)
            .dueDate(now.minusDays(1))
            .taskList(list)
            .build();
    t1.changeStatus(TaskStatus.OPEN);
    taskService.createTask(t1);

    Task t2 =
        Task.builder()
            .title("UI-Tests abschließen")
            .description("Task wurde abgeschlossen")
            .priority(TaskPriority.HIGH)
            .dueDate(now.minusDays(2))
            .taskList(list)
            .build();
    t2.changeStatus(TaskStatus.COMPLETED);
    taskService.createTask(t2);

    Task t3 =
        Task.builder()
            .title("Swagger veröffentlichen")
            .description("Task wurde abgeschlossen")
            .priority(TaskPriority.LOW)
            .dueDate(null)
            .taskList(list)
            .build();
    t3.changeStatus(TaskStatus.COMPLETED);
    taskService.createTask(t3);
  }

  private <T> T randomEnum(Class<T> enumClass) {
    T[] values = enumClass.getEnumConstants();
    return values[random.nextInt(values.length)];
  }
}
