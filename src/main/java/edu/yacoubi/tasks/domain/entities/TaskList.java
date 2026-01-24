package edu.yacoubi.tasks.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    // -----------------------------------------
    @Builder
    private TaskList(String title, String description) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title darf nicht leer sein.");
        }

        this.title = title;
        this.description = description;
        this.status = TaskListStatus.ACTIVE;

        // DDD: Domain entscheidet über Zeitstempel
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    // -----------------------------------------
    //  Domain-Methoden
    // -----------------------------------------

    public void archive() {
        if (this.status == TaskListStatus.ARCHIVED) {
            return; // idempotent
        }
        this.status = TaskListStatus.ARCHIVED;
        this.updated = LocalDateTime.now();
    }

    public void activate() {
        if (this.status == TaskListStatus.ACTIVE) {
            return;
        }
        this.status = TaskListStatus.ACTIVE;
        this.updated = LocalDateTime.now();
    }

    public boolean isArchived() {
        return this.status == TaskListStatus.ARCHIVED;
    }

    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task darf nicht null sein.");
        }
        tasks.add(task);
        this.updated = LocalDateTime.now();
    }

    public void removeTask(final Task task) {
        if (!tasks.contains(task)) {
            throw new EntityNotFoundException("Task does not belong to TaskList");
        }
        tasks.remove(task);
        this.updated = LocalDateTime.now();
    }

    public boolean ownsTask(Task task) {
        return this.getTasks().contains(task);
    }

    public void rename(String newTitle) {
        if (newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException("Title darf nicht leer sein.");
        }
        this.title = newTitle;
        this.updated = LocalDateTime.now();
    }

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

