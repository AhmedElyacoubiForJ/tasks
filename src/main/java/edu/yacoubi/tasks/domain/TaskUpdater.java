package edu.yacoubi.tasks.domain;

import edu.yacoubi.tasks.domain.dto.request.task.PatchTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.FullUpdateTaskDto;
import edu.yacoubi.tasks.domain.entities.Task;
import org.springframework.stereotype.Component;

/**
 * Verantwortlich für das Anwenden von Update-DTOs auf die Task-Domain-Entity.
 *
 * DDD:
 * - Der Updater ruft ausschließlich Domain-Methoden auf (keine Setter!).
 * - Die Domain bleibt der einzige Ort, an dem Regeln und Invarianten geprüft werden.
 * - Der Updater selbst enthält KEINE fachliche Logik.
 *
 * Architektur:
 * - Der Orchestrator entscheidet, ob Full- oder Patch-Update angewendet wird.
 * - Der Updater ist ein reiner "Mapper" zwischen DTO und Domain-Methoden.
 */
@Component
public class TaskUpdater {

    /**
     * Wendet ein vollständiges Update (PUT) auf einen Task an.
     *
     * Regeln:
     * - Alle Felder im DTO sind Pflichtfelder.
     * - Es werden nur Änderungen angewendet, wenn sich der Wert tatsächlich unterscheidet.
     *   → verhindert unnötige Domain-Events, updated-Timestamps, etc.
     * - Statusänderungen laufen über task.changeStatus(), das selbst Domain-Regeln prüft.
     */
    public void applyFullUpdate(Task task, FullUpdateTaskDto dto) {

        // Titel aktualisieren
        if (!dto.title().equals(task.getTitle())) {
            task.changeTitle(dto.title());
        }

        // Beschreibung aktualisieren
        if (!dto.description().equals(task.getDescription())) {
            task.changeDescription(dto.description());
        }

        // Fälligkeitsdatum aktualisieren
        if (!dto.dueDate().equals(task.getDueDate())) {
            task.changeDueDate(dto.dueDate());
        }

        // Priorität aktualisieren
        if (!dto.priority().equals(task.getPriority())) {
            task.changePriority(dto.priority());
        }

        // Status aktualisieren (immer zuletzt)
        // Domain-Methode prüft selbst ungültige Transitionen
        if (!dto.status().equals(task.getStatus())) {
            task.changeStatus(dto.status());
        }
    }

    /**
     * Wendet ein partielles Update (PATCH) auf einen Task an.
     *
     * Regeln:
     * - Nur Felder, die im DTO gesetzt sind (nicht null), werden aktualisiert.
     * - Auch hier werden nur echte Änderungen angewendet.
     * - Statusänderungen laufen über task.changeStatus().
     */
    public void applyPatch(Task task, PatchTaskDto dto) {

        if (dto.title() != null && !dto.title().equals(task.getTitle())) {
            task.changeTitle(dto.title());
        }

        if (dto.description() != null && !dto.description().equals(task.getDescription())) {
            task.changeDescription(dto.description());
        }

        if (dto.dueDate() != null && !dto.dueDate().equals(task.getDueDate())) {
            task.changeDueDate(dto.dueDate());
        }

        if (dto.priority() != null && !dto.priority().equals(task.getPriority())) {
            task.changePriority(dto.priority());
        }

        if (dto.status() != null && !dto.status().equals(task.getStatus())) {
            task.changeStatus(dto.status());
        }
    }
}
