package edu.yacoubi.tasks.services.app;

import edu.yacoubi.tasks.domain.dto.request.tasklist.CreateTaskListDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.TaskListFilterDto;
import edu.yacoubi.tasks.domain.dto.request.tasklist.UpdateTaskListDto;
import edu.yacoubi.tasks.domain.dto.response.tasklist.TaskListDto;
import edu.yacoubi.tasks.domain.entities.TaskList;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

/**
 * Service für das TaskList-Aggregat.
 *
 * <p>
 * In Domain-Driven Design (DDD) ist ein <strong>Aggregat</strong> eine Gruppe
 * fachlich zusammengehöriger Entitäten, die gemeinsam Konsistenz garantieren.
 * Die <strong>Aggregat-Root</strong> ist die einzige Entität, über die externe
 * Operationen auf das Aggregat ausgeführt werden dürfen.
 * </p>
 *
 * <p>
 * In diesem Projekt ist <strong>TaskList</strong> die Aggregat-Root.
 * Alle Operationen, die ausschließlich die TaskList betreffen, werden in diesem
 * Service gekapselt.
 * </p>
 *
 * <p>
 * Cross-Aggregate-UseCases (z.B. Archivieren einer TaskList abhängig vom Zustand
 * der Tasks) werden bewusst <strong>nicht</strong> hier implementiert, sondern im
 * {@code ITaskListsTaskOrchestrator}, um Aggregatsgrenzen nicht zu verletzen.
 * </p>
 */
public interface ITaskListService {

    /**
     * Liefert alle TaskLists zurück.
     *
     * <p>
     * Reine Aggregat-Operation: Es werden ausschließlich TaskLists geladen,
     * ohne Bezug zu Tasks oder anderen Aggregaten.
     * </p>
     *
     * @return Liste aller TaskLists
     */
    List<TaskList> getAllTaskLists();

    /**
     * Liefert alle aktiven (nicht archivierten) TaskLists zurück.
     *
     * <p>
     * Reine Aggregat-Operation: Die Filterung erfolgt ausschließlich anhand
     * des TaskList-Zustands.
     * </p>
     *
     * @return Liste aktiver TaskLists
     */
    List<TaskList> getActiveTaskLists();

    /**
     * Liefert alle archivierten TaskLists zurück.
     *
     * <p>
     * Reine Aggregat-Operation: Keine Cross-Aggregate-Logik.
     * </p>
     *
     * @return Liste archivierter TaskLists
     */
    List<TaskList> getArchivedTaskLists();

    /**
     * Liefert TaskLists anhand eines Filterobjekts paginiert zurück.
     *
     * <p>
     * Reine Aggregat-Operation: Die Filterung basiert ausschließlich auf
     * Eigenschaften der TaskList selbst.
     * </p>
     *
     * @param params Filterparameter
     * @return paginierte Liste von TaskListDto
     */
    Page<TaskListDto> getFilteredTaskLists(TaskListFilterDto params);

    /**
     * Liefert eine TaskList anhand ihrer ID zurück oder wirft eine Exception,
     * falls sie nicht existiert.
     *
     * <p>
     * Reine Aggregat-Operation: Wird häufig vom Orchestrator genutzt, um
     * Cross-Aggregate-UseCases vorzubereiten.
     * </p>
     *
     * @param id ID der TaskList
     * @return gefundene TaskList
     *
     * @throws EntityNotFoundException wenn keine TaskList mit der angegebenen UUID existiert
     */
    TaskList getTaskListOrThrow(UUID id);

    /**
     * Erstellt eine neue TaskList.
     *
     * <p>
     * Reine Aggregat-Operation: Es wird nur die TaskList selbst erzeugt.
     * </p>
     *
     * @param taskListDto Daten zur Erstellung
     * @return erstellte TaskList
     */
    TaskList createTaskList(CreateTaskListDto taskListDto);

    /**
     * Löscht eine TaskList anhand ihrer ID.
     *
     * <p>
     * Reine Aggregat-Operation. Falls beim Löschen Tasks berücksichtigt werden
     * müssen (z.B. Soft-Delete), wäre dies Cross-Aggregate-Logik und würde
     * in den Orchestrator wandern.
     * </p>
     *
     * @param id ID der TaskList
     */
    void deleteTaskList(UUID id);

    /**
     * Aktualisiert eine bestehende TaskList.
     *
     * <p>
     * Reine Aggregat-Operation: Es werden ausschließlich Eigenschaften der
     * TaskList selbst verändert.
     * </p>
     *
     * @param id  ID der TaskList
     * @param dto Daten für das Update
     * @return aktualisierte TaskList
     */
    TaskList updateTaskList(UUID id, UpdateTaskListDto dto);

  /**
   * Aktiviert eine TaskList anhand ihrer ID.
   *
   * <p>
   * Reine Aggregat-Operation: Die Aktivierung betrifft ausschließlich die
   * TaskList selbst. Es werden keine Tasks geladen oder geprüft.
   * </p>
   *
   * <p>
   * Domain-Regel:
   * Eine TaskList kann jederzeit wieder aktiviert werden. Eine neu erstellte
   * TaskList ist standardmäßig aktiv.
   * </p>
   *
   * @param id ID der TaskList
   * @return aktivierte TaskList
   *
   * @throws EntityNotFoundException wenn keine TaskList mit der angegebenen UUID existiert
   */
  // Diese Methode gehört korrekt in den Aggregat‑Service, weil: Aktivieren betrifft nur TaskList
  //
  // Keine Cross‑Aggregate‑Regel
  //
  // Keine Task‑Prüfung
  //
  // Reiner Zustandswechsel → Domain‑Logik
  TaskList activateTaskList(UUID id);
}
