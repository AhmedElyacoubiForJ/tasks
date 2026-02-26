package edu.yacoubi.tasks.api.tasklists.scenario;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * TaskListsScenarioTestSuite
 * --------------------------
 * Führt ALLE Tests im Package
 *   "edu.yacoubi.tasks.api.tasklists.scenario"
 * aus – inklusive aller Unterpackages.
 *
 * Aufruf (CLI):
 *   ./mvnw test -Dtest=TaskListsScenarioTestSuite
 *
 * Welche Testklassen werden ausgeführt?
 *   - TaskListsScenarioControllerTest
 *
 * Abgedeckte Endpunkte (ITaskListsScenarioApi):
 *
 *   GET    /api/tasklists/active
 *     → getActiveTaskLists()
 *
 *   GET    /api/tasklists/archived
 *     → getArchivedTaskLists()
 *
 *   POST   /api/tasklists/{id}/archive
 *     → archiveTaskList(UUID id)
 *
 * Zweck:
 *   Diese Suite ist für End-to-End- und Szenario-Tests gedacht,
 *   die mehrere Schritte oder Controller kombinieren.
 *
 * Typische Szenarien:
 *   * TaskList erstellen → Task hinzufügen → Task aktualisieren → Task löschen
 *   * TaskList archivieren → prüfen, ob sie in "archived" erscheint
 *   * aktive vs. archivierte Listen vergleichen
 *
 * Ideal für komplexe API-Flows, die mehrere Endpunkte verbinden.
 */
@Suite
@SelectPackages("edu.yacoubi.tasks.api.tasklists.scenario")
public class TaskListsScenarioTestSuite {
}
