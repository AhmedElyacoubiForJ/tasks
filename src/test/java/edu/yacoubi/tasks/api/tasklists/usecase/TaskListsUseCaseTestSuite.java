package edu.yacoubi.tasks.api.tasklists.usecase;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * TaskListsUseCaseTestSuite
 * --------------------------
 * Führt ALLE Tests im Package
 *   "edu.yacoubi.tasks.api.tasklists.usecase"
 * aus – inklusive aller Unterpackages.
 *
 * Aufruf (CLI):
 *   ./mvnw test -Dtest=TaskListsUseCaseTestSuite
 *
 * Welche Testklassen werden ausgeführt?
 *   - TaskListsUseCaseControllerHappyPathTest
 *   - TaskListsUseCaseControllerDomainErrorTest
 *   - TaskListsUseCaseControllerTechnicalErrorTest
 *
 * Abgedeckte Endpunkte (ITaskListsUseCaseApi):
 *
 *   PUT    /api/tasklists/{id}/archive
 *     → archiveTaskList(UUID id)
 *
 *   POST   /api/tasklists/{id}/activate
 *     → activateTaskList(UUID id)
 *
 *   PATCH  /api/tasklists/{id}/status
 *     → changeTaskListStatus(UUID id, ...)
 *
 *   POST   /api/tasklists/{id}/tasks
 *     → createTask(UUID listId, ...)
 *
 *   PATCH  /api/tasklists/{id}/tasks/{taskId}/status
 *     → changeTaskStatus(UUID listId, UUID taskId, ...)
 *
 *   DELETE /api/tasklists/{id}/tasks/{taskId}
 *     → removeTask(UUID listId, UUID taskId)
 *
 * Zweck:
 *   Diese Suite führt alle Use-Case-bezogenen Tests aus, die
 *   fachliche Abläufe, Domainregeln und technische Fehler der
 *   TaskList- und Task-Use-Cases prüfen.
 *
 * Enthaltene Testarten:
 *   * Happy-Path-Use-Cases (200 / 204)
 *   * Domainregel-Verletzungen (409 / 422)
 *   * Technische Fehler (400 / 415)
 *
 * NICHT enthalten:
 *   - CRUD-Tests (200 / 201 / 400 / 404 / 405)
 *   - Szenario-/End-to-End-Tests
 *   - Aggregatübergreifende Flows
 */
@Suite
@SelectPackages("edu.yacoubi.tasks.api.tasklists.usecase")
public class TaskListsUseCaseTestSuite {
}
