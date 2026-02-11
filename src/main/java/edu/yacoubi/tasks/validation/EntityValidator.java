package edu.yacoubi.tasks.validation;

import edu.yacoubi.tasks.repositories.TaskListRepository;
import edu.yacoubi.tasks.repositories.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Zentraler Validator für die Existenz von Entitäten.
 *
 * <p>
 * Dieser Validator stellt sicher, dass bestimmte Entitäten im System existieren,
 * bevor weitere Operationen ausgeführt werden. Er kapselt die reine
 * Existenzprüfung und verhindert damit doppelte Logik in Services oder
 * Orchestratoren.
 * </p>
 *
 * <p>
 * Wichtiger Hinweis: Dieser Validator prüft ausschließlich die Existenz.
 * Fachliche Regeln (z.B. "TaskList darf nicht archiviert sein") gehören
 * weiterhin in den Orchestrator oder in die Domain-Logik.
 * </p>
 *
 * <p>
 * Vorteile dieses Validators:
 * <ul>
 *     <li>Entkopplung der Services voneinander</li>
 *     <li>Vermeidung redundanter Repository-Abfragen</li>
 *     <li>Einheitliche Fehlerbehandlung</li>
 *     <li>Verbesserte Testbarkeit</li>
 * </ul>
 * </p>
 *
 * @author
 *     A. El Yacoubi
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EntityValidator {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    /**
     * Prüft, ob ein Task mit der angegebenen ID existiert.
     *
     * @param taskId die ID des Tasks
     * @throws EntityNotFoundException wenn kein Task mit dieser ID existiert
     */
    public void validateTaskExists(final UUID taskId) {
        log.info("::validateTaskExists gestartet mit taskId={}", taskId);

        if (!taskRepository.existsById(taskId)) {
            String errorMessage = "Task nicht gefunden mit ID: " + taskId;
            log.error("::validateTaskExists Fehler: {}", errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }

        log.info("::validateTaskExists erfolgreich abgeschlossen");
    }

    /**
     * Prüft, ob eine TaskList mit der angegebenen ID existiert.
     *
     * @param taskListId die ID der TaskList
     * @throws EntityNotFoundException wenn keine TaskList mit dieser ID existiert
     */
    public void validateTaskListExists(final UUID taskListId) {
        log.info("::validateTaskListExists gestartet mit taskListId={}", taskListId);

        if (!taskListRepository.existsById(taskListId)) {
            String errorMessage = "TaskList nicht gefunden mit ID: " + taskListId;
            log.error("::validateTaskListExists Fehler: {}", errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }

        log.info("::validateTaskListExists erfolgreich abgeschlossen");
    }
}
