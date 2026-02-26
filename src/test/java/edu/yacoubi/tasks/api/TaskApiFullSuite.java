package edu.yacoubi.tasks.api;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * TaskApiFullSuite
 * ----------------
 * Diese Suite führt ALLE API-bezogenen Tests im gesamten Package
 * "edu.yacoubi.tasks.api" aus – inklusive aller Unterpackages.
 *
 * Aufruf (CLI):
 *   ./mvnw test -Dtest=TaskApiFullSuite
 *
 * Welche Tests werden ausgeführt?
 *   - TaskListsCrudTestSuite
 *   - TaskListsTasksTestSuite
 *   - TaskListsScenarioTestSuite
 *   - sowie alle Testklassen in deren Unterpackages:
 *       * TaskListsCrudControllerHappyPathTest
 *       * TaskListsCrudControllerNotFoundTest
 *       * TaskListsCrudControllerValidationTest
 *       * TaskListsCrudControllerDomainTest
 *
 *       * TaskListsTasksControllerHappyPathTest
 *       * TaskListsTasksControllerNotFoundTest
 *       * TaskListsTasksControllerValidationTest
 *       * TaskListsTasksControllerDomainTest
 *
 *       * TaskListsScenarioControllerTest
 *
 * Diese Suite ist ideal für vollständige API-Regressionstests.
 */
@Suite
@SelectPackages("edu.yacoubi.tasks.api")
public class TaskApiFullSuite {
}
