package edu.yacoubi.tasks.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

/**
 * Aggregat-Root des Task-Management-Systems.
 *
 * <p>Diese Klasse folgt den Prinzipien von Domain-Driven Design (DDD):
 * <ul>
 *     <li>Sie ist der Aggregat-Root des TaskList-Aggregats.</li>
 *     <li>Sie kapselt fachliches Verhalten und schützt Invarianten.</li>
 *     <li>Tasks können nur über Domain-Methoden hinzugefügt oder entfernt werden.</li>
 *     <li>Statusänderungen erfolgen kontrolliert über Methoden wie {@code archive()}.</li>
 * </ul>
 * </p>
 */
@Entity
@Table(name = "task_lists")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA only
@EqualsAndHashCode(of = "id")
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskListStatus status;

    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;


    // -----------------------------------------
    //  DDD: Builder setzt alle Invarianten
    //  Der Builder ist bewusst PRIVATE, damit:
    //   - keine andere Schicht (Controller/Service/Mapper) das Aggregat direkt erzeugen kann
    //   - die Domain selbst kontrolliert, wie gültige TaskLists entstehen
    //   - alle Invarianten (z. B. Titel darf nicht leer sein) garantiert geprüft werden
    //   - das Aggregat nur über definierte Fabriken/Builder erzeugt wird
    // -----------------------------------------
    @Builder
    private TaskList(String title, String description) {

        // DDD: Invariante – eine TaskList muss immer einen gültigen Titel haben
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title darf nicht leer sein.");
        }

        this.title = title;
        this.description = description;

        // DDD: Domain definiert den initialen Zustand
        this.status = TaskListStatus.ACTIVE;

        // DDD: Domain entscheidet über Zeitstempel, nicht der Client
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }


    // -----------------------------------------
    //  Domain-Methoden (fachliches Verhalten)
    //  Jede Methode schützt Invarianten und
    //  stellt sicher, dass das Aggregat immer
    //  in einem gültigen Zustand bleibt.
    // -----------------------------------------

    /**
     * Archiviert die TaskList.
     *
     * DDD:
     * - Statusänderungen dürfen nur über Domain-Methoden erfolgen.
     * - Die Methode ist idempotent: mehrfaches Aufrufen ändert nichts.
     * - updated wird automatisch gesetzt, um den Lebenszyklus zu dokumentieren.
     */
    public void archive() {
        if (this.status == TaskListStatus.ARCHIVED) {
            return; // idempotent
        }
        this.status = TaskListStatus.ARCHIVED;
        this.updated = LocalDateTime.now();
    }

    /**
     * Aktiviert eine archivierte TaskList wieder.
     *
     * DDD:
     * - Nur die Domain entscheidet, wann eine Liste aktiv sein darf.
     * - Auch diese Methode ist idempotent.
     */
    public void activate() {
        if (this.status == TaskListStatus.ACTIVE) {
            return;
        }
        this.status = TaskListStatus.ACTIVE;
        this.updated = LocalDateTime.now();
    }

    /**
     * Hilfsmethode für Domain-Logik.
     * Kein Getter für Status, sondern eine semantische Frage:
     * "Ist die Liste archiviert?"
     */
    public boolean isArchived() {
        return this.status == TaskListStatus.ARCHIVED;
    }

    /**
     * Fügt einen Task zur TaskList hinzu.
     *
     * DDD:
     * - Tasks dürfen nur über das Aggregat hinzugefügt werden.
     * - Dadurch wird sichergestellt, dass die Beziehung konsistent bleibt.
     * - updated wird gesetzt, da sich der Zustand des Aggregats ändert.
     */
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task darf nicht null sein.");
        }
        tasks.add(task);
        this.updated = LocalDateTime.now();
    }

    /**
     * Entfernt einen Task aus der TaskList.
     *
     * DDD:
     * - Entfernen ist nur erlaubt, wenn der Task wirklich dazugehört.
     * - Aggregat schützt seine Konsistenz.
     */
    public void removeTask(final Task task) {
        if (!tasks.contains(task)) {
            throw new EntityNotFoundException("Task does not belong to TaskList");
        }
        tasks.remove(task);
        this.updated = LocalDateTime.now();
    }

    /**
     * Prüft, ob ein Task zu dieser TaskList gehört.
     *
     * DDD:
     * - Hilfsmethode für Orchestratoren/Services.
     */
    public boolean ownsTask(Task task) {
        return this.getTasks().contains(task);
    }

    /**
     * Ändert den Titel der TaskList.
     *
     * DDD:
     * - Titel ist eine Invariante → darf nie leer sein.
     * - Nur die Domain darf Titel ändern.
     */
    public void rename(String newTitle) {
        if (newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException("Title darf nicht leer sein.");
        }
        this.title = newTitle;
        this.updated = LocalDateTime.now();
    }

    /**
     * Ändert die Beschreibung der TaskList.
     *
     * DDD:
     * - Beschreibung ist optional, aber jede Änderung ist ein Domain-Ereignis.
     */
    public void changeDescription(String newDescription) {
        this.description = newDescription;
        this.updated = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "TaskList{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", created=" + created +
                ", updated=" + updated +
                ", taskCount=" + tasks.size() +
                '}';
    }
}
