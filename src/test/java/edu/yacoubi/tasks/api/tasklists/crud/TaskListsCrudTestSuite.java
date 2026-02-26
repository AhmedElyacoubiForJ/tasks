package edu.yacoubi.tasks.api.tasklists.crud;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * TaskListsCrudTestSuite
 * ----------------------
 * Führt ALLE Tests im Package
 *   "edu.yacoubi.tasks.api.tasklists.crud"
 * aus – inklusive aller Unterpackages.
 *
 * Aufruf (CLI):
 *   ./mvnw test -Dtest=TaskListsCrudTestSuite
 *
 * Welche Testklassen werden ausgeführt?
 *   - TaskListsCrudControllerHappyPathTest
 *   - TaskListsCrudControllerNotFoundTest
 *   - TaskListsCrudControllerValidationTest
 *   - TaskListsCrudControllerDomainTest
 *
 * Abgedeckte Endpunkte (TaskList-CRUD):
 *
 *   GET    /api/tasklists
 *     → getAllTaskLists()
 *
 *   GET    /api/tasklists/{id}
 *     → getTaskList(UUID id)
 *
 *   POST   /api/tasklists
 *     → createTaskList(CreateTaskListDto dto)
 *
 *   PUT    /api/tasklists/{id}
 *     → updateTaskList(UUID id, UpdateTaskListDto dto)
 *
 *   PATCH  /api/tasklists/{id}
 *     → patchTaskList(UUID id, PatchTaskListDto dto)
 *
 *   DELETE /api/tasklists/{id}
 *     → deleteTaskList(UUID id)
 *
 * Zweck:
 *   Diese Suite testet die vollständige CRUD-Funktionalität der TaskList-API
 *   und stellt sicher, dass Happy-Path, Fehlerfälle, Validierung und
 *   Domainregeln korrekt implementiert sind.
 */
@Suite
@SelectPackages("edu.yacoubi.tasks.api.tasklists.crud")
public class TaskListsCrudTestSuite {
}
