package edu.yacoubi.tasks.api.tasklists.tasks;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * TaskListsTasksTestSuite
 * -----------------------
 * Führt ALLE Tests im Package
 *   "edu.yacoubi.tasks.api.tasklists.tasks"
 * aus – inklusive aller Unterpackages.
 *
 * Aufruf (CLI):
 *   ./mvnw test -Dtest=TaskListsTasksTestSuite
 *
 * Welche Testklassen werden ausgeführt?
 *   - TaskListsTasksControllerHappyPathTest
 *   - TaskListsTasksControllerNotFoundTest
 *   - TaskListsTasksControllerValidationTest
 *   - TaskListsTasksControllerDomainTest
 *
 * Abgedeckte Endpunkte (ITaskListsTasksApiImpl):
 *
 *   GET    /api/tasklists/{taskListId}/tasks
 *     → getTasksByListId(UUID taskListId)
 *
 *   POST   /api/tasklists/{taskListId}/tasks
 *     → createTaskInList(UUID taskListId, CreateTaskDto dto)
 *
 *   PUT    /api/tasklists/{taskListId}/tasks/{taskId}
 *     → updateTaskInList(UUID taskListId, UUID taskId, FullUpdateTaskDto dto)
 *
 *   PATCH  /api/tasklists/{taskListId}/tasks/{taskId}
 *     → patchTaskInList(UUID taskListId, UUID taskId, PatchTaskDto dto)
 *
 *   DELETE /api/tasklists/{taskListId}/tasks/{taskId}
 *     → deleteTaskInList(UUID taskListId, UUID taskId)
 *
 * Zweck:
 *   Diese Suite deckt die vollständige Task-bezogene API ab und stellt sicher,
 *   dass Happy-Path, Fehlerfälle, Validierung und Domainregeln korrekt
 *   implementiert sind.
 */
@Suite
@SelectPackages("edu.yacoubi.tasks.api.tasklists.tasks")
public class TaskListsTasksTestSuite {
}
