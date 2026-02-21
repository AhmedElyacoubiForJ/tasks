package edu.yacoubi.tasks.domain;

import edu.yacoubi.tasks.domain.dto.request.tasklist.UpdateTaskListDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.PatchTaskListDto;
import edu.yacoubi.tasks.domain.entities.TaskList;
import org.springframework.stereotype.Component;

/**
 * ============================================================
 * 🧠 DDD-GEBOTE FÜR DEN TASKLIST-UPDATER
 * ============================================================
 *
 * ✔ Der Updater ruft ausschließlich Domain-Methoden auf
 *   → rename(), changeDescription(), changeStatus()
 *
 * ✔ Der Updater enthält KEINE fachliche Logik
 *   → keine Archivierungsregeln
 *   → keine Aktivierungsregeln
 *
 * ✔ Der Updater entscheidet NICHT, ob ein Statuswechsel erlaubt ist
 *   → das macht die Domain (TaskList-Entity)
 *
 * ✔ Der Updater ist ein reiner "DTO → Domain"-Mapper
 *   → überträgt nur Werte, die sich wirklich geändert haben
 *
 * ✔ PATCH und PUT sind klar getrennt
 *   → PUT = alle Felder Pflicht
 *   → PATCH = nur gesetzte Felder anwenden
 *
 * Dies ist DDD in Reinform.
 * ============================================================
 */
@Component
public class TaskListUpdater {

    /**
     * Wendet ein vollständiges Update (PUT) auf eine TaskList an.
     *
     * Regeln:
     * - Alle Felder im DTO sind Pflichtfelder.
     * - Nur echte Änderungen werden angewendet.
     */
    public void applyFullUpdate(TaskList list, UpdateTaskListDto dto) {

        // Titel
        if (!dto.title().equals(list.getTitle())) {
            list.rename(dto.title());
        }

        // Beschreibung
        if (!safeEquals(dto.description(), list.getDescription())) {
            list.changeDescription(dto.description());
        }

        // Status (Domain entscheidet, ob erlaubt)
        if (dto.status() != null && dto.status() != list.getStatus()) {
            list.changeStatus(dto.status());
        }
    }

    /**
     * Wendet ein partielles Update (PATCH) auf eine TaskList an.
     *
     * Regeln:
     * - Nur Felder, die im DTO gesetzt sind (nicht null), werden aktualisiert.
     * - Nur echte Änderungen werden angewendet.
     */
    public void applyPatch(TaskList list, PatchTaskListDto dto) {

        if (dto.title() != null && !dto.title().equals(list.getTitle())) {
            list.rename(dto.title());
        }

        if (dto.description() != null && !safeEquals(dto.description(), list.getDescription())) {
            list.changeDescription(dto.description());
        }

        if (dto.status() != null && dto.status() != list.getStatus()) {
            list.changeStatus(dto.status());
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
