package edu.yacoubi.tasks.api.tasklists.tasks.crud;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * TaskListsTasksCrudTestSuite
 * ---------------------------
 * Führt ALLE CRUD-bezogenen API-Regressionstests für:
 *
 *   /api/tasklists/{taskListId}/tasks
 *
 * aus – inklusive aller Unterpackages unter:
 *
 *   edu.yacoubi.tasks.api.tasklists.tasks.crud
 *
 * Abgedeckte Testkategorien:
 *   - HappyPath (200 / 201 / 204)
 *   - Validation (400)
 *   - NotFound (404)
 *   - MethodNotAllowed (405)
 *
 * NICHT enthalten:
 *   - Domain-Fehler (409 / 422)
 *   - UseCase-bezogene Tests
 *
 * Aufruf (CLI):
 *   ./mvnw test -Dtest=TaskListsTasksCrudTestSuite
 */
@Suite
@SelectPackages("edu.yacoubi.tasks.api.tasklists.tasks.crud")
public class TaskListsTasksCrudTestSuite {
}
