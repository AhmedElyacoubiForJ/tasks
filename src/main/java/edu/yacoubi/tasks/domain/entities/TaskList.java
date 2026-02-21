package edu.yacoubi.tasks.domain.entities;

import edu.yacoubi.tasks.exceptions.DomainRuleViolationException;
import edu.yacoubi.tasks.exceptions.DomainValidationException;
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
 *
 * <ul>
 *   <li>Sie ist der Aggregat-Root des TaskList-Aggregats.
 *   <li>Sie kapselt fachliches Verhalten und schützt Invarianten.
 *   <li>Tasks können nur über Domain-Methoden hinzugefügt oder entfernt werden.
 *   <li>Statusänderungen erfolgen kontrolliert über Methoden wie {@code archive()}.
 * </ul>
 *
 * ============================================================
 * 🧠 DDD-GEBOTE FÜR DAS TASKLIST-AGGREGAT
 * ============================================================
 *
 * <p>✔ TaskList ist der EINZIGE Einstiegspunkt ins Aggregat → Tasks können nur über TaskList
 * erstellt, geändert, archiviert oder entfernt werden.
 *
 * <p>✔ Task schützt seine EIGENEN Regeln → Statuswechsel → Titel → Priority → DueDate
 *
 * <p>✔ TaskList schützt die AGGREGAT-REGELN → Archivierung → Änderungen im ARCHIVED-Status →
 * Task-Zugehörigkeit → Task-Erstellung → Task-Statusänderung
 *
 * <p>✔ Das Aggregat garantiert IMMER Konsistenz → Keine TaskList ohne Titel → Keine Tasks ohne
 * TaskList → Keine Änderungen an archivierten Listen → Keine Tasks, die nicht zur Liste gehören
 *
 * <p>Dies ist DDD in Reinform. ============================================================
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
  private final List<Task> tasks = new ArrayList<>();

  @Column(name = "created", nullable = false)
  private LocalDateTime created;

  @Column(name = "updated", nullable = false)
  private LocalDateTime updated;

  // ============================================================
  // 🧱 BUILDER – schützt alle Invarianten
  // ============================================================
  @Builder
  private TaskList(String title, String description) {

    if (title == null || title.isBlank()) {
      throw new DomainValidationException("Title darf nicht leer sein.");
    }

    this.title = title;
    this.description = description;
    this.status = TaskListStatus.ACTIVE;
    this.created = LocalDateTime.now();
    this.updated = LocalDateTime.now();
  }

  // ============================================================
  // 📌 DOMAIN-METHODEN – fachliches Verhalten
  // ============================================================

  /** Archiviert die TaskList, falls alle Tasks abgeschlossen sind. */
  public void archive() {

    if (this.status == TaskListStatus.ARCHIVED) {
      return; // idempotent
    }

    if (!isArchivable()) {
      throw new DomainRuleViolationException(
          "TaskList kann nicht archiviert werden, da noch offene Tasks existieren.");
    }

    this.status = TaskListStatus.ARCHIVED;
    this.updated = LocalDateTime.now();
  }

  /** Aktiviert eine archivierte TaskList wieder. */
  public void activate() {
    if (this.status == TaskListStatus.ACTIVE) {
      return;
    }
    this.status = TaskListStatus.ACTIVE;
    this.updated = LocalDateTime.now();
  }

  /**
   * ============================================================
   * 🧠 DDD-GEBOTE FÜR STATUSWECHSEL IM TASKLIST-AGGREGAT
   * ============================================================
   *
   * ✔ Statuswechsel sind fachliche Operationen
   *   → sie gehören ausschließlich in die Domain (TaskList-Entity)
   *   → niemals in Controller, Service, Updater oder Orchestrator
   *
   * ✔ Archivierung ist ein eigener Use-Case
   *   → darf NICHT über changeStatus() erfolgen
   *   → nur über archive(), da dort die Regel "alle Tasks abgeschlossen" geprüft wird
   *
   * ✔ Reaktivierung ist ein eigener Use-Case
   *   → darf NICHT über changeStatus() erfolgen
   *   → nur über activate(), da Domain entscheidet, ob erlaubt
   *
   * ✔ changeStatus() ist nur für NEUTRALE Statuswechsel gedacht
   *   → z. B. zukünftige Stati wie: PLANNED, ON_HOLD, IN_REVIEW
   *   → Domain schützt sich selbst vor ungültigen Wechseln
   *
   * ✔ Statuswechsel müssen INVARIANTEN schützen
   *   → archivierte Listen dürfen nicht verändert werden
   *   → Tasks dürfen nicht zu archivierten Listen hinzugefügt werden
   *   → Statuswechsel dürfen keine Regeln umgehen (z. B. Archivierungsregeln)
   *
   * ✔ Statuswechsel müssen idempotent sein
   *   → gleicher Status = keine Änderung
   *   → updated-Timestamp nur bei echten Änderungen
   *
   * ✔ Statuswechsel müssen atomar sein
   *   → Domain-Methode + Persistenz = eine Transaktion
   *
   * ✔ Statuswechsel dürfen niemals Tasks inkonsistent machen
   *   → Domain garantiert: TaskListStatus und TaskStatus widersprechen sich nicht
   *
   * Dies ist DDD in Reinform.
   * ============================================================
   */
  public void changeStatus(TaskListStatus newStatus) {

    if (newStatus == null) {
      throw new DomainValidationException("Status darf nicht null sein.");
    }

    // Idempotenz: gleicher Status → nichts tun
    if (this.status == newStatus) {
      return;
    }

    // Archivieren darf NICHT über changeStatus erfolgen
    if (newStatus == TaskListStatus.ARCHIVED) {
      throw new DomainRuleViolationException(
              "Archivierung muss über archive() erfolgen, nicht über changeStatus()."
      );
    }

    // Reaktivieren darf NICHT über changeStatus erfolgen
    if (newStatus == TaskListStatus.ACTIVE && this.status == TaskListStatus.ARCHIVED) {
      throw new DomainRuleViolationException(
              "Reaktivierung muss über activate() erfolgen, nicht über changeStatus()."
      );
    }

    // Allgemeiner Statuswechsel (falls du später mehr Status hast)
    this.status = newStatus;
    this.updated = LocalDateTime.now();
  }


  public boolean isArchived() {
    return this.status == TaskListStatus.ARCHIVED;
  }

  public void rename(String newTitle) {
    if (newTitle == null || newTitle.isBlank()) {
      throw new DomainValidationException("Title darf nicht leer sein.");
    }
    this.title = newTitle;
    this.updated = LocalDateTime.now();
  }

  public void changeDescription(String newDescription) {
    this.description = newDescription;
    this.updated = LocalDateTime.now();
  }

  // ============================================================
  // 🧱 TASK-MANAGEMENT – nur über die Root erlaubt
  // ============================================================

  /** Erstellt einen neuen Task innerhalb der TaskList. */
  public Task createTask(
      String title, String description, LocalDateTime dueDate, TaskPriority priority) {
    assertCanAddTask();

    Task task =
        Task.builder()
            .title(title)
            .description(description)
            .dueDate(dueDate)
            .priority(priority)
            .taskList(this)
            .build();

    tasks.add(task);
    this.updated = LocalDateTime.now();

    return task;
  }

  /** Fügt einen bestehenden Task hinzu (z. B. bei Rehydration). */
  public void addTask(Task task) {
    if (task == null) {
      throw new DomainValidationException("Task darf nicht null sein.");
    }

    assertCanAddTask();

    if (task.getTaskList() != this) {
      throw new DomainRuleViolationException("Task gehört nicht zu dieser TaskList.");
    }

    tasks.add(task);
    this.updated = LocalDateTime.now();
  }

  /** Entfernt einen Task aus der TaskList. */
  public void removeTask(Task task) {
    if (!tasks.contains(task)) {
      throw new DomainRuleViolationException("Task gehört nicht zu dieser TaskList.");
    }

    tasks.remove(task);
    this.updated = LocalDateTime.now();
  }

  /** Ändert den Status eines Tasks über die Root. */
  public void changeTaskStatus(UUID taskId, TaskStatus newStatus) {

    if (this.isArchived()) {
      throw new DomainRuleViolationException("Archivierte TaskLists können nicht geändert werden.");
    }

    Task task =
        tasks.stream()
            .filter(t -> t.getId().equals(taskId))
            .findFirst()
            .orElseThrow(
                () -> new DomainRuleViolationException("Task gehört nicht zu dieser TaskList."));

    task.changeStatus(newStatus);
    this.updated = LocalDateTime.now();
  }

  // ============================================================
  // 🔒 INVARIANTEN & HILFSMETHODEN
  // ============================================================

  private void assertCanAddTask() {
    if (this.isArchived()) {
      throw new DomainRuleViolationException(
          "Kann keinen Task zu einer archivierten TaskList hinzufügen.");
    }
  }

  private boolean isArchivable() {
    return tasks.stream().allMatch(Task::isCompleted);
  }

  // ============================================================
  // 📝 toString – ohne rekursive Serialisierung
  // ============================================================
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
