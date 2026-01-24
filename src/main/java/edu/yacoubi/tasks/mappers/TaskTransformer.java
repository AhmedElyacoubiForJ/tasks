package edu.yacoubi.tasks.mappers;

import edu.yacoubi.tasks.domain.dto.response.task.TaskDetailDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.Task;

/**
 * Zentrale Transformationslogik für Task-Entities.
 *
 * <p>Diese Klasse bündelt alle Transformationen zwischen dem Task-Aggregat
 * und seinen DTO-Repräsentationen. Sie arbeitet explizit und ohne Reflection
 * oder Code-Generierung, um Domain-Regeln und Aggregatsgrenzen vollständig
 * unter Kontrolle zu halten.</p>
 */
public final class TaskTransformer {

    private TaskTransformer() {
        // Utility-Klasse – keine Instanzen
    }

    /**
     * Transformer zur Umwandlung einer {@link Task}-Domain-Entity
     * in ein {@link TaskSummaryDto}.
     *
     * <p>Dieser Transformer extrahiert nur die zur Übersicht notwendigen Daten
     * und gibt keine verschachtelten Strukturen zurück. Die TaskList wird
     * ausschließlich durch ihre ID referenziert, um Aggregatsgrenzen zu
     * respektieren und keine unnötigen Lazy-Loads auszulösen.</p>
     *
     * <p><strong>Hinweis:</strong>
     * Diese Transformation wird aktuell NICHT in der Methode
     * {@code TaskServiceImpl.findByTaskListId(...)} verwendet, da das Repository
     * dort bereits ein {@link TaskSummaryDto} direkt per JPQL-Projection liefert.
     * Der Transformer bleibt dennoch bestehen, da er für andere Anwendungsfälle
     * (z. B. wenn Tasks als Entities geladen werden) weiterhin nützlich ist und
     * eine konsistente, explizite Transformationslogik sicherstellt.</p>
     *
     * <p>Der Null-Check für {@code task.getTaskList()} ist rein defensiv.
     * Im Domain-Modell ist eine TaskList niemals null (optional=false),
     * aber Tests oder zukünftige Änderungen könnten dies beeinflussen.</p>
     */
    public static final ITransformer<Task, TaskSummaryDto> TASK_TO_SUMMARY =
            task -> new TaskSummaryDto(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getDueDate(),
                    task.getPriority(),
                    task.getStatus(),
                    task.getTaskList() != null ? task.getTaskList().getId() : null
            );

    /**
     * Transformer zur Umwandlung einer {@link Task}-Domain-Entity
     * in ein {@link TaskDetailDto}.
     *
     * <p>Dieser Transformer liefert alle relevanten Detailinformationen einer Aufgabe,
     * einschließlich der Zeitstempel {@code created} und {@code updated}. Die TaskList
     * wird ausschließlich über ihre ID referenziert, um Aggregatsgrenzen zu respektieren
     * und keine verschachtelten Strukturen zurückzugeben.</p>
     *
     * <p><strong>Hinweis:</strong>
     * Dieser Transformer wird typischerweise in Detail-Endpunkten verwendet, bei denen
     * eine Task-Entity über das Repository geladen wird (z. B. {@code findById}).
     * Im Gegensatz zur Summary-Variante existiert hier keine JPQL-Projection, daher
     * erfolgt die Transformation explizit über diesen Transformer.</p>
     *
     * <p>Der Null-Check für {@code task.getTaskList()} ist rein defensiv. Im Domain-Modell
     * ist eine TaskList aufgrund von {@code optional = false} niemals null, aber Tests
     * oder zukünftige Änderungen könnten dies beeinflussen.</p>
     */
    public static final ITransformer<Task, TaskDetailDto> TASK_TO_DETAIL =
            task -> new TaskDetailDto(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getDueDate(),
                    task.getStatus(),
                    task.getPriority(),
                    task.getCreated(),
                    task.getUpdated(),
                    task.getTaskList() != null ? task.getTaskList().getId() : null
            );
}
