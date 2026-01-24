package edu.yacoubi.tasks.mappers;

/**
 * Utility-Klasse für allgemeine Transformationsmethoden im Tasks-Domain-Modell.
 *
 * <p>Diese Klasse stellt eine universelle Methode zur Verfügung, um beliebige Transformationen
 * mithilfe des {@link ITransformer} Functional Interface durchzuführen. Sie ermöglicht es,
 * Domain-Entities in DTOs (oder umgekehrt) zu transformieren, ohne dass die Transformationslogik
 * an mehreren Stellen dupliziert werden muss.</p>
 *
 * <p>Das Utility ist besonders nützlich in DDD-Architekturen, in denen Entities keine Setter
 * besitzen und ausschließlich über Builder oder Domain-Methoden verändert werden dürfen.
 * Die Transformation bleibt dadurch explizit, kontrolliert und vollständig transparent.</p>
 *
 * <p><strong>Beispielverwendung (Tasks-Projekt):</strong></p>
 * <pre>{@code
 * // Transformer: Task → TaskSummaryDto
 * ITransformer<Task, TaskSummaryDto> taskToSummary = task ->
 *         new TaskSummaryDto(
 *                 task.getId(),
 *                 task.getTitle(),
 *                 task.getDescription(),
 *                 task.getDueDate(),
 *                 task.getPriority(),
 *                 task.getStatus(),
 *                 task.getTaskList().getId()
 *         );
 *
 * // Anwendung der Transformation
 * TaskSummaryDto dto = TransformerUtil.transform(taskToSummary, taskInstance);
 * }</pre>
 *
 * <p>Die Klasse kann nicht instanziiert werden, da der Konstruktor privat ist.</p>
 */
public class TransformerUtil {

    private TransformerUtil() {
        // Privater Konstruktor, um Instanziierung zu verhindern
    }

    /**
     * Wendet den übergebenen {@link ITransformer} auf die gegebene Entität an.
     *
     * <p>Diese Methode nimmt eine Entität und einen {@link ITransformer} entgegen und gibt die
     * transformierte Instanz zurück. Sie eignet sich für alle Transformationen zwischen
     * Domain-Entities und DTOs.</p>
     *
     * <p><strong>Beispiel:</strong></p>
     * <pre>{@code
     * TaskSummaryDto dto = TransformerUtil.transform(taskToSummary, task);
     * }</pre>
     *
     * @param transformer Functional Interface zur Transformation
     * @param entity      die zu transformierende Entität
     * @param <T>         Eingabetyp (z. B. Task)
     * @param <R>         Rückgabetyp (z. B. TaskSummaryDto)
     * @return transformierte Instanz
     * @throws IllegalArgumentException wenn transformer oder entity null sind
     */
    public static <T, R> R transform(ITransformer<T, R> transformer, T entity) {
        if (transformer == null || entity == null) {
            throw new IllegalArgumentException("Transformer and entity must not be null");
        }
        return transformer.transform(entity);
    }
}
