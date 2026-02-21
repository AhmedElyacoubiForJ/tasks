package edu.yacoubi.tasks.domain;

import edu.yacoubi.tasks.domain.dto.request.task.PatchTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.FullUpdateTaskDto;
import edu.yacoubi.tasks.domain.entities.Task;
import org.springframework.stereotype.Component;

/**
 * ============================================================
 * 🧠 DDD-GEBOTE FÜR DEN TASK-UPDATER
 * ============================================================
 *
 * ✔ Der Updater ruft ausschließlich Domain-Methoden auf
 *   → keine Setter, keine direkte Feldmanipulation.
 *
 * ✔ Der Updater enthält KEINE fachliche Logik
 *   → keine Statusregeln, keine Validierungen.
 *
 * ✔ Der Updater entscheidet NICHT, ob ein Update erlaubt ist
 *   → das macht die Domain (Task-Entity).
 *
 * ✔ Der Updater ist ein reiner "DTO → Domain"-Mapper
 *   → er überträgt nur Werte, die sich wirklich geändert haben.
 *
 * Dies ist DDD in Reinform.
 * ============================================================
 */
@Component
public class TaskUpdater {

    /**
     * Wendet ein vollständiges Update (PUT) auf einen Task an.
     *
     * Regeln:
     * - Alle Felder im DTO sind Pflichtfelder.
     * - Es werden nur echte Änderungen angewendet.
     * - Statusänderungen laufen über task.changeStatus().
     */
    public void applyFullUpdate(Task task, FullUpdateTaskDto dto) {

        // Titel
        if (!dto.title().equals(task.getTitle())) {
            task.changeTitle(dto.title());
        }

        // Beschreibung
        if (!safeEquals(dto.description(), task.getDescription())) {
            task.changeDescription(dto.description());
        }

        // Fälligkeitsdatum
        if (!safeEquals(dto.dueDate(), task.getDueDate())) {
            task.changeDueDate(dto.dueDate());
        }

        // Priorität
        if (!dto.priority().equals(task.getPriority())) {
            task.changePriority(dto.priority());
        }

        // Status (immer zuletzt)
        if (!dto.status().equals(task.getStatus())) {
            task.changeStatus(dto.status());
        }
    }

    /**
     * Wendet ein partielles Update (PATCH) auf einen Task an.
     *
     * Regeln:
     * - Nur Felder, die im DTO gesetzt sind (nicht null), werden aktualisiert.
     * - Nur echte Änderungen werden angewendet.
     */
    public void applyPatch(Task task, PatchTaskDto dto) {

        if (dto.title() != null && !dto.title().equals(task.getTitle())) {
            task.changeTitle(dto.title());
        }

        if (dto.description() != null && !safeEquals(dto.description(), task.getDescription())) {
            task.changeDescription(dto.description());
        }

        if (dto.dueDate() != null && !safeEquals(dto.dueDate(), task.getDueDate())) {
            task.changeDueDate(dto.dueDate());
        }

        if (dto.priority() != null && !dto.priority().equals(task.getPriority())) {
            task.changePriority(dto.priority());
        }

        if (dto.status() != null && !dto.status().equals(task.getStatus())) {
            task.changeStatus(dto.status());
        }
    }

    /**
     * Null-sicherer Vergleich für optionale Felder.
     */
    private boolean safeEquals(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}
