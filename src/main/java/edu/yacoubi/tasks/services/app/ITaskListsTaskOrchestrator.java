package edu.yacoubi.tasks.services.app;

import edu.yacoubi.tasks.domain.dto.request.task.CreateTaskDto;
import edu.yacoubi.tasks.domain.dto.request.task.UpdateTaskDto;
import edu.yacoubi.tasks.domain.dto.response.task.TaskSummaryDto;
import edu.yacoubi.tasks.domain.entities.TaskList;

import java.util.List;
import java.util.UUID;

/**
 * Orchestrator-Service für UseCases, die sowohl das TaskList- als auch das Task-Aggregat betreffen.
 *
 * <p>
 * In Domain-Driven Design (DDD) ist ein <strong>Aggregat</strong> eine Gruppe von
 * fachlich zusammengehörigen Entitäten, die gemeinsam Konsistenz garantieren.
 * Ein Aggregat besitzt genau eine <strong>Aggregat-Root</strong>, über die alle
 * Änderungen am Aggregat erfolgen müssen.
 * </p>
 *
 * <p>
 * In diesem Projekt ist <strong>TaskList</strong> das Aggregat-Root.
 * Eine TaskList besitzt mehrere Tasks, aber Tasks dürfen niemals unabhängig
 * von ihrer TaskList verändert werden.
 * </p>
 *
 * <p>
 * Der Orchestrator wird immer dann eingesetzt, wenn ein UseCase
 * <strong>mehr als ein Aggregat</strong> betrifft — also sogenannte
 * <strong>Cross-Aggregate-Logik</strong>.
 * Beispiele:
 * <ul>
 *     <li>Beim Erstellen eines Tasks muss zuerst geprüft werden, ob die TaskList existiert.</li>
 *     <li>Beim Archivieren einer TaskList müssen zuerst alle zugehörigen Tasks geprüft werden.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Der Orchestrator verhindert, dass Controller oder einzelne Services
 * fachliche Regeln doppelt implementieren oder Aggregatsgrenzen verletzen.
 * </p>
 */
public interface ITaskListsTaskOrchestrator {

    /**
     * Erstellt einen neuen Task innerhalb einer bestehenden TaskList.
     *
     * <p>
     * Dies ist ein Cross-Aggregate-UseCase, weil:
     * <ul>
     *     <li>zuerst das TaskList-Aggregat validiert werden muss (existiert die TaskList?)</li>
     *     <li>dann das Task-Aggregat erweitert wird (Task wird erstellt)</li>
     * </ul>
     * </p>
     *
     * @param taskListId ID der TaskList, zu der der Task hinzugefügt wird
     * @param dto        Daten für den neuen Task
     * @return Zusammenfassung des erstellten Tasks
     */
    TaskSummaryDto createTaskInList(UUID taskListId, CreateTaskDto dto);

    /**
     * Aktualisiert einen bestehenden Task innerhalb einer bestimmten TaskList.
     * <p>
     * Diese Methode repräsentiert den Anwendungsfall "Update Task in List" und
     * delegiert die Ablaufsteuerung an den Orchestrator. Der Orchestrator lädt
     * das Aggregate Root (TaskList), prüft die Zugehörigkeit des Tasks und führt
     * die fachliche Aktualisierung über Domain-Methoden aus. Anschließend wird
     * das Aggregate persistiert und das aktualisierte TaskSummaryDto zurückgegeben.
     *
     * @param taskListId die UUID der TaskList, zu der der Task gehört
     * @param taskId     die UUID des zu aktualisierenden Tasks
     * @param dto        die Daten für die Aktualisierung (nur gesetzte Felder werden übernommen)
     * @return das aktualisierte TaskSummaryDto
     *
//     * @throws NotFoundException   wenn TaskList oder Task nicht existieren
//     * @throws DomainException     wenn der Task nicht zur TaskList gehört oder fachliche Regeln verletzt
     */
    TaskSummaryDto updateTaskInList(UUID taskListId, UUID taskId, UpdateTaskDto dto);

    /**
     * Löscht einen bestehenden Task aus einer bestimmten TaskList.
     * <p>
     * Diese Methode repräsentiert den Anwendungsfall "Delete Task in List" und
     * delegiert die Ablaufsteuerung an den Orchestrator. Der Orchestrator lädt
     * das Aggregate Root (TaskList), prüft die Zugehörigkeit des Tasks und führt
     * die fachliche Löschoperation über das Domain-Model aus. Anschließend wird
     * das Aggregate persistiert.
     *
     * @param taskListId die UUID der TaskList, aus der der Task gelöscht werden soll
     * @param taskId     die UUID des zu löschenden Tasks
     *
//     * @throws NotFoundException   wenn TaskList oder Task nicht existieren
//     * @throws DomainException     wenn der Task nicht zur TaskList gehört
     */
    void deleteTaskInList(UUID taskListId, UUID taskId);


    /**
     * Liefert alle Tasks einer TaskList zurück.
     *
     * <p>
     * Cross-Aggregate-Logik, weil:
     * <ul>
     *     <li>zuerst das TaskList-Aggregat validiert wird</li>
     *     <li>dann Tasks aus dem Task-Aggregat geladen werden</li>
     * </ul>
     * </p>
     *
     * @param taskListId ID der TaskList
     * @return Liste der zugehörigen Tasks
     */
    List<TaskSummaryDto> getTasksByListId(UUID taskListId);

    /**
     * Archiviert eine TaskList ausschließlich, wenn alle zugehörigen Tasks abgeschlossen sind.
     *
     * <p>
     * Dies ist ein klassischer Cross-Aggregate-UseCase:
     * <ul>
     *     <li>Das Archivieren betrifft das TaskList-Aggregat</li>
     *     <li>Die Entscheidung, ob archiviert werden darf, hängt vom Task-Aggregat ab</li>
     * </ul>
     * </p>
     *
     * <p>
     * Der Orchestrator stellt sicher, dass die Regel
     * <strong>"Eine TaskList darf nur archiviert werden, wenn alle Tasks abgeschlossen sind"</strong>
     * zentral und konsistent umgesetzt wird.
     * </p>
     *
     * @param taskListId ID der TaskList
     * @return die archivierte TaskList
     */
    TaskList archiveTaskListIfTasksCompleted(UUID taskListId);

    /**
     * Prüft, ob eine TaskList archiviert werden darf.
     *
     * <p>
     * Cross-Aggregate-Logik, weil:
     * <ul>
     *     <li>die Entscheidung vom Zustand der Tasks abhängt</li>
     *     <li>aber das Archivieren selbst eine Operation des TaskList-Aggregats ist</li>
     * </ul>
     * </p>
     *
     * <p>
     * Diese Methode wird häufig vom Controller oder anderen Services genutzt,
     * um UI- oder API-Entscheidungen zu treffen (z.B. "Archivieren"-Button anzeigen).
     * </p>
     *
     * @param taskListId ID der TaskList
     * @return true, wenn alle Tasks abgeschlossen sind
     */
    boolean isArchivable(UUID taskListId);
}
