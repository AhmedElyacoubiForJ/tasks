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

        for (int i = 1; i <= 5; i++) {
            TaskList list = new TaskList();
            list.setTitle("📋 Demo-Liste #" + i);
            list.setDescription("Dies ist die " + i + ". generierte TaskList");
            list.setCreated(now);
            list.setUpdated(now);

            Task task1 = new Task();
            task1.setTitle("Aufgabe " + i + ".1");
            task1.setDescription("Beschreibung zur Aufgabe 1 in Liste " + i);
            task1.setPriority(TaskPriority.HIGH);
            task1.setStatus(i % 2 == 0 ? TaskStatus.CLOSED : TaskStatus.OPEN);
            task1.setDueDate(now.plusDays(3));
            task1.setTaskList(list);

            Task task2 = new Task();
            task2.setTitle("Aufgabe " + i + ".2");
            task2.setDescription("Beschreibung zur Aufgabe 2 in Liste " + i);
            task2.setPriority(TaskPriority.LOW);
            task2.setStatus(TaskStatus.OPEN);
            task2.setDueDate(now.plusDays(5));
            task2.setTaskList(list);

            list.setTasks(List.of(task1, task2));
            taskListRepository.save(list);
        }

        log.info("✅ Mehrere Demo-Listen erfolgreich eingefügt");
    }
}
