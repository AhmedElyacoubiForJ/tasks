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
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializerService {

    private final TaskListRepository taskListRepository;

    @PostConstruct
    public void init() {
        log.info("🚀 Datenbank-Initialisierung gestartet");

        if (taskListRepository.count() > 0) {
            log.info("📦 Daten bereits vorhanden — Initialisierung wird übersprungen");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        Random random = new Random();

        for (int i = 1; i <= 6; i++) {
            TaskList list = new TaskList();
            list.setTitle("📋 Demo-Liste #" + i);
            list.setDescription("Generierte Liste mit zufälligen Tasks");
            list.setCreated(now.minusDays(random.nextInt(10)));
            list.setUpdated(now);

            // Generiere 3 Tasks pro Liste
            List<Task> tasks = List.of(
                    createRandomTask("Aufgabe " + i + ".1", list, random),
                    createRandomTask("Aufgabe " + i + ".2", list, random),
                    createRandomTask("Aufgabe " + i + ".3", list, random)
            );

            list.setTasks(tasks);
            taskListRepository.save(list);
        }

        log.info("✅ Zufällig variierte Listen erfolgreich eingefügt");
    }

    private Task createRandomTask(String title, TaskList list, Random random) {
        TaskPriority[] priorities = TaskPriority.values();
        TaskStatus[] statuses = TaskStatus.values();

        Task task = new Task();
        task.setTitle(title);
        task.setDescription("Auto-generierte Task mit Zufallswerten");
        task.setPriority(priorities[random.nextInt(priorities.length)]);
        task.setStatus(statuses[random.nextInt(statuses.length)]);
        task.setDueDate(LocalDateTime.now().plusDays(1 + random.nextInt(10)));
        task.setTaskList(list);
        return task;
    }

}
