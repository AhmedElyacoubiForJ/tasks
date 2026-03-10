package edu.yacoubi.tasks.api;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * TaskApiFullSuite
 * ----------------
 * Führt ALLE API-bezogenen Tests im gesamten Package
 *   "edu.yacoubi.tasks.api"
 * aus – inklusive aller Unterpackages.
 *
 * Aufruf (CLI):
 *   ./mvnw test -Dtest=TaskApiFullSuite
 *
 * Welche Tests werden ausgeführt?
 *   - TaskListsCrudTestSuite
 *   - TaskListsTasksCrudTestSuite
 *   - TaskListsUseCaseTestSuite
 *   - sowie alle Testklassen in deren Unterpackages:
 *       * CRUD-Tests (HappyPath, Validation, NotFound, MethodNotAllowed)
 *       * Use-Case-Tests (HappyPath, DomainError, TechnicalError)
 *
 * Diese Suite ist ideal für vollständige API-Regressionstests
 * über alle Schichten: CRUD + UseCases.
 */
@Suite
@SelectPackages("edu.yacoubi.tasks.api")
public class TaskApiFullTestSuite {
}
