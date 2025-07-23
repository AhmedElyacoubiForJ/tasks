package edu.yacoubi.tasks.servcies.init;

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

        // Prüfe ob schon Daten vorhanden sind
        if (taskListRepository.count() > 0) {
            log.info("📦 Daten bereits vorhanden — Initialisierung wird übersprungen");
            return;
        }

        // Timestamps generieren
        LocalDateTime now = LocalDateTime.now();

        // TaskList erstellen
        TaskList list = new TaskList();
        list.setTitle("🏁 Demo-Liste");
        list.setDescription("Beispielhafte Aufgaben für Swagger & Seeding");
        list.setCreated(now);
        list.setUpdated(now);

        // Tasks zuweisen
        Task task1 = new Task();
        task1.setTitle("Swagger konfigurieren");
        task1.setDescription("Swagger-Dokumentation für Task-Endpunkte erstellen");
        task1.setPriority(TaskPriority.HIGH);
        task1.setStatus(TaskStatus.OPEN);
        task1.setDueDate(LocalDateTime.now().plusDays(5));
        task1.setTaskList(list); // Beziehung setzen

        Task task2 = new Task();
        task2.setTitle("Seed-Daten initialisieren");
        task2.setDescription("Initiale Tasks beim App-Start in die Datenbank schreiben");
        task2.setPriority(TaskPriority.MEDIUM);
        task2.setStatus(TaskStatus.CLOSED);
        task2.setDueDate(LocalDateTime.now().plusDays(2));
        task2.setTaskList(list);


        // Liste mit Tasks verknüpfen
        list.setTasks(List.of(task1, task2));

        // Speichern
        taskListRepository.save(list);

        log.info("✅ Demo-Daten erfolgreich eingefügt");
    }
}
