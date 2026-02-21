package edu.yacoubi.tasks.services.init;

import edu.yacoubi.tasks.domain.dto.request.tasklist.CreateTaskListDto;
import edu.yacoubi.tasks.domain.entities.Task;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.domain.entities.TaskPriority;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import edu.yacoubi.tasks.services.app.ITaskListService;
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

      TaskList list = taskListService.createTaskList(
              new CreateTaskListDto(
                      "📋 TaskList #" + i,
                      "Auto-generierte Liste für Demo-Zwecke"
              )
      );

      // Liste 3 und 6 bleiben leer
      if (i == 3 || i == 6) {
        continue;
      }

      // Sonst 3 Tasks erzeugen – über Aggregat-Root
      createRandomTask("Aufgabe " + i + ".1", list, true);
      createRandomTask("Aufgabe " + i + ".2", list, false);
      createRandomTask("Aufgabe " + i + ".3", list, random.nextBoolean());

      // Aggregat speichern
      taskListService.save(list);
    }

    // Eine vollständige Liste erzeugen
    createCompleteList();

    log.info("✅ Demo-Daten erfolgreich gespeichert");
  }

  private void createRandomTask(String title, TaskList list, boolean withDueDate) {

    Task task = list.createTask(
            title,
            "Generierter Task: " + title,
            withDueDate ? now.plusDays(random.nextInt(14) + 1) : null,
            randomEnum(TaskPriority.class)
    );

    // Status nachträglich setzen (Domain-Methode)
    task.changeStatus(randomEnum(TaskStatus.class));
  }

  private void createCompleteList() {

    TaskList list = taskListService.createTaskList(
            new CreateTaskListDto(
                    "📋 TaskList #7 - 100% erledigt",
                    "Alle Aufgaben sind abgeschlossen"
            )
    );

    Task t1 = list.createTask(
            "Dokumentation finalisieren",
            "Task wurde abgeschlossen",
            now.minusDays(1),
            TaskPriority.MEDIUM
    );
    t1.changeStatus(TaskStatus.OPEN);

    Task t2 = list.createTask(
            "UI-Tests abschließen",
            "Task wurde abgeschlossen",
            now.minusDays(2),
            TaskPriority.HIGH
    );
    t2.changeStatus(TaskStatus.COMPLETED);

    Task t3 = list.createTask(
            "Swagger veröffentlichen",
            "Task wurde abgeschlossen",
            null,
            TaskPriority.LOW
    );
    t3.changeStatus(TaskStatus.COMPLETED);

    // Aggregat speichern
    taskListService.save(list);
  }

  private <T> T randomEnum(Class<T> enumClass) {
    T[] values = enumClass.getEnumConstants();
    return values[random.nextInt(values.length)];
  }
}
