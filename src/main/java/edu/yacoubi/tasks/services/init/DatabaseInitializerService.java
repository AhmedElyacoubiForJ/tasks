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

    /**
     * Initialisiert die Datenbank beim Anwendungsstart mit mehreren TaskLists.
     * Enthält Aufgaben mit zufälligem Status/Priorität, einige ohne Fälligkeitsdatum,
     * sowie Listen mit 0 Tasks zur UI-Validierung.
     */
    @PostConstruct
    public void init() {
        log.info("🚀 Datenbank-Initialisierung gestartet");

        // Wenn bereits Daten vorhanden sind, Abbruch
        if (taskListRepository.count() > 0) {
            log.info("📦 Daten bereits vorhanden — Initialisierung wird übersprungen");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        Random random = new Random();

        for (int i = 1; i <= 6; i++) {
            // Neue TaskList erzeugen
            TaskList list = new TaskList();
            list.setTitle("📋 TaskList #" + i);
            list.setDescription("Auto-generierte Liste für Demo-Zwecke");
            list.setCreated(now.minusDays(random.nextInt(10))); // zufälliges Erstellungsdatum
            list.setUpdated(now);

            List<Task> tasks;

            if (i == 3 || i == 6) {
                // Liste ohne Aufgaben → nützlich für UI-Tests, Fortschritt = 0%
                tasks = List.of();
            } else {
                // Liste mit 3 Aufgaben, gemischt mit und ohne Fälligkeitsdatum
                tasks = List.of(
                        createRandomTask("Aufgabe " + i + ".1", list, random, true),           // mit dueDate
                        createRandomTask("Aufgabe " + i + ".2", list, random, false),          // ohne dueDate
                        createRandomTask("Aufgabe " + i + ".3", list, random, random.nextBoolean()) // zufällig
                );
            }

            list.setTasks(tasks);
            taskListRepository.save(list);
        }

        log.info("✅ Demo-Listen erfolgreich gespeichert");
    }

    /**
     * Hilfsmethode zur Erzeugung einer einzelnen Aufgabe mit Zufallswerten.
     *
     * @param title       Titel der Aufgabe
     * @param list        Zugehörige TaskList
     * @param random      Zufallsinstanz
     * @param withDueDate ob eine Fälligkeitsdatum gesetzt werden soll
     * @return konfigurierte Task-Instanz
     */
    private Task createRandomTask(String title, TaskList list, Random random, boolean withDueDate) {
        TaskPriority[] priorities = TaskPriority.values();
        TaskStatus[] statuses = TaskStatus.values();

        Task task = new Task();
        task.setTitle(title);
        task.setDescription("Generierter Task: " + title);
        task.setPriority(priorities[random.nextInt(priorities.length)]);
        task.setStatus(statuses[random.nextInt(statuses.length)]);
        task.setTaskList(list);

        if (withDueDate) {
            task.setDueDate(LocalDateTime.now().plusDays(random.nextInt(14) + 1));
        } else {
            task.setDueDate(null); // keine Fälligkeit
        }

        return task;
    }
}
