package edu.yacoubi.tasks.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

/**
 * Domain-Entity, die eine einzelne Aufgabe innerhalb einer TaskList repräsentiert.
 *
 * <p>Diese Klasse folgt den Prinzipien von Domain-Driven Design (DDD):
 * <ul>
 *     <li>Sie ist Teil des Aggregats "TaskList" und darf niemals alleine existieren.</li>
 *     <li>Sie besitzt fachliches Verhalten (Domain-Methoden) statt Setter.</li>
 *     <li>Invarianten werden durch den privaten Builder geschützt.</li>
 *     <li>Status- und Prioritätsänderungen erfolgen ausschließlich über Domain-Methoden.</li>
 * </ul>
 * </p>
 *
 * <p>Die Klasse ist vollständig JPA-kompatibel, nutzt jedoch keine Setter,
 * um unkontrollierte Mutationen zu verhindern. Änderungen erfolgen ausschließlich
 * über Domain-Methoden wie {@code changeStatus()}, {@code changePriority()},
 * {@code changeTitle()} oder {@code changeDueDate()}.</p>
 *
 * <p>Der Builder stellt sicher, dass Pflichtfelder wie Titel, Priority und TaskList
 * gesetzt werden und dass die Entity niemals in einem ungültigen Zustand entsteht.</p>
 *
 * <p>Die toString()-Methode gibt nur die ID der TaskList aus, um rekursive
 * Serialisierung und LazyLoading-Probleme zu vermeiden.</p>
 */
@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA only
@AllArgsConstructor(access = AccessLevel.PRIVATE) // verhindert unkontrollierte Instanziierung
@EqualsAndHashCode(of = "id")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    // -----------------------------------------
    //  DDD: Task gehört IMMER zu einer TaskList
    //  optional = false → JPA garantiert: niemals null
    //  nullable = false → DB garantiert: niemals null
    //  fetch = LAZY → verhindert unnötiges Laden der TaskList
    // -----------------------------------------
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_list_id", nullable = false)
    private TaskList taskList;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;


    // -----------------------------------------
    //  DDD: Privater Builder schützt Invarianten
    //  Nur die Domain darf Tasks erzeugen.
    //  Controller/Service können keine inkonsistenten Tasks bauen.
    // -----------------------------------------
    @Builder
    private Task(
            String title,
            String description,
            LocalDateTime dueDate,
            TaskPriority priority,
            TaskList taskList
    ) {
        // Pflichtfeld: TaskList
        if (taskList == null) {
            throw new IllegalArgumentException("TaskList darf nicht null sein.");
        }

        // Pflichtfeld: Titel
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title darf nicht leer sein.");
        }

        // Pflichtfeld: Priority
        if (priority == null) {
            throw new IllegalArgumentException("Priority darf nicht null sein.");
        }

        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.taskList = taskList;

        // DDD: Domain definiert initialen Status
        this.status = TaskStatus.OPEN;

        // DDD: Domain setzt Zeitstempel
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }


    // -----------------------------------------
    //  Domain-Methoden (fachliches Verhalten)
    //  Keine Setter → Aggregat bleibt konsistent
    // -----------------------------------------

    /**
     * Ändert den Titel des Tasks.
     *
     * DDD:
     * - Titel ist eine Invariante → darf nie leer sein.
     * - Jede Änderung ist ein Domain-Ereignis → updated wird gesetzt.
     */
    public void changeTitle(String newTitle) {
        if (newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException("Titel darf nicht leer sein.");
        }
        this.title = newTitle;
        this.updated = LocalDateTime.now();
    }

    /**
     * Ändert die Beschreibung des Tasks.
     *
     * DDD:
     * - Beschreibung ist optional, aber jede Änderung ist ein Domain-Ereignis.
     */
    public void changeDescription(String newDescription) {
        this.description = newDescription;
        this.updated = LocalDateTime.now();
    }

    /**
     * Ändert das Fälligkeitsdatum.
     *
     * DDD:
     * - Keine Invariante, aber jede Änderung beeinflusst den Task-Lebenszyklus.
     */
    public void changeDueDate(LocalDateTime newDueDate) {
        this.dueDate = newDueDate;
        this.updated = LocalDateTime.now();
    }

    /**
     * Ändert die Priorität des Tasks.
     *
     * DDD:
     * - Priority ist eine Invariante → darf nie null sein.
     */
    public void changePriority(TaskPriority newPriority) {
        if (newPriority == null) {
            throw new IllegalArgumentException("Priority darf nicht null sein.");
        }
        this.priority = newPriority;
        this.updated = LocalDateTime.now();
    }

    /**
     * Ändert den Status des Tasks.
     *
     * DDD:
     * - Statusänderungen sind fachlich relevant.
     * - Ungültige Transitionen werden verhindert.
     * - Methode ist idempotent.
     */
    public void changeStatus(TaskStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status darf nicht null sein.");
        }

        // Keine Änderung → kein Update nötig
        if (this.status == newStatus) {
            return;
        }

        // Verbotene Transitionen (fachliche Regeln)
        if (this.status == TaskStatus.COMPLETED && newStatus == TaskStatus.OPEN) {
            throw new IllegalStateException("Ein abgeschlossener Task kann nicht wieder geöffnet werden.");
        }

        if (this.status == TaskStatus.COMPLETED && newStatus == TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException("Ein abgeschlossener Task kann nicht wieder in Bearbeitung gesetzt werden.");
        }

        if (this.status == TaskStatus.IN_PROGRESS && newStatus == TaskStatus.OPEN) {
            throw new IllegalStateException("Ein Task in Bearbeitung kann nicht wieder geöffnet werden.");
        }

        // Erlaubte Transition
        this.status = newStatus;
        this.updated = LocalDateTime.now();
    }

    /**
     * Hilfsmethode für Domain-Logik.
     */
    public boolean isCompleted() {
        return this.status == TaskStatus.COMPLETED;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                ", created=" + created +
                ", updated=" + updated +
                ", taskListId=" + (taskList != null ? taskList.getId() : null) +
                '}';
    }
}
