package edu.yacoubi.tasks.services.init;

import edu.yacoubi.tasks.domain.entities.Task;
import edu.yacoubi.tasks.domain.entities.TaskList;
import edu.yacoubi.tasks.domain.entities.TaskPriority;
import edu.yacoubi.tasks.domain.entities.TaskStatus;
import edu.yacoubi.tasks.repositories.TaskListRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializerService {

    private final TaskListRepository taskListRepository;
    private final Random random = new Random();
    private final LocalDateTime now = LocalDateTime.now();

    @PostConstruct
    public void init() {
        log.info("🚀 Initialisiere Demo-Daten");

        if (taskListRepository.count() > 0) {
            log.info("📦 Datenbank enthält bereits TaskLists – Initialisierung wird übersprungen");
            return;
        }

        for (int i = 1; i <= 6; i++) {
            TaskList list = createTaskList("📋 TaskList #" + i, "Auto-generierte Liste für Demo-Zwecke");

            if (i == 3 || i == 6) {
                list.setTasks(List.of()); // leer für UI-Tests
            } else {
                list.setTasks(List.of(
                        createTask("Aufgabe " + i + ".1", list, true),
                        createTask("Aufgabe " + i + ".2", list, false),
                        createTask("Aufgabe " + i + ".3", list, random.nextBoolean())
                ));
            }

            taskListRepository.save(list);
        }

        taskListRepository.save(createCompleteList());

        log.info("✅ Demo-Daten erfolgreich gespeichert");
    }

    private TaskList createTaskList(String title, String description) {
        TaskList list = new TaskList();
        list.setTitle(title);
        list.setDescription(description);
        list.setCreated(now.minusDays(random.nextInt(10)));
        list.setUpdated(now);
        return list;
    }

    private Task createTask(String title, TaskList list, boolean withDueDate) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription("Generierter Task: " + title);
        task.setPriority(randomEnum(TaskPriority.class));
        task.setStatus(randomEnum(TaskStatus.class));
        task.setTaskList(list);

        if (withDueDate) {
            task.setDueDate(now.plusDays(random.nextInt(14) + 1));
        }

        return task;
    }

    private TaskList createCompleteList() {
        TaskList list = createTaskList("📋 TaskList #7 - 100% erledigt", "Alle Aufgaben sind abgeschlossen");

        List<Task> tasks = new ArrayList<>();

        tasks.add(createFixedTask("Dokumentation finalisieren", TaskPriority.MEDIUM, TaskStatus.OPEN, now.minusDays(1), list));
        tasks.add(createFixedTask("UI-Tests abschließen", TaskPriority.HIGH, TaskStatus.CLOSED, now.minusDays(2), list));
        tasks.add(createFixedTask("Swagger veröffentlichen", TaskPriority.LOW, TaskStatus.CLOSED, null, list));

        list.setTasks(tasks);
        return list;
    }

    private Task createFixedTask(String title, TaskPriority priority, TaskStatus status, LocalDateTime dueDate, TaskList list) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription("Task wurde abgeschlossen");
        task.setPriority(priority);
        task.setStatus(status);
        task.setDueDate(dueDate);
        task.setTaskList(list);
        return task;
    }

    private <T> T randomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[random.nextInt(values.length)];
    }
}
