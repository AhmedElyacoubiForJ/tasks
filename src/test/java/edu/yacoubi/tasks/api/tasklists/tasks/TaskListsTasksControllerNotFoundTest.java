package edu.yacoubi.tasks.api.tasklists.tasks;

import edu.yacoubi.tasks.api.base.TaskApiRestAssuredTestBase;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * TaskListsTasksControllerNotFoundTest
 * ------------------------------------
 * Testet alle 404-Fehlerfälle für Endpunkte unter:
 *
 *   /api/tasklists/{taskListId}/tasks
 *
 * Aufruf (CLI):
 *   ./mvnw test -Dtest=TaskListsTasksControllerNotFoundTest
 *
 * Abgedeckte Endpunkte:
 *   - GET    /api/tasklists/{taskListId}/tasks
 *   - DELETE /api/tasklists/{taskListId}/tasks/{taskId}
 *
 * Zweck:
 *   Sicherstellen, dass die API korrekt mit nicht existierenden
 *   TaskLists oder Tasks umgeht und konsistente 404-Fehler liefert.
 */
class TaskListsTasksControllerNotFoundTest extends TaskApiRestAssuredTestBase {

    // ------------------------------------------------------------
    // GET /api/tasklists/{taskListId}/tasks
    // → TaskList existiert NICHT → 404
    // ------------------------------------------------------------
    @Test
    void testGetTasksByListId_NotFound() {
        given()
                .when()
                .get("/tasklists/" + UUID.randomUUID() + "/tasks")
                .then()
                .log().body()
                .statusCode(404)
                .body("message", equalTo("Ressource nicht gefunden"));
    }

    // ------------------------------------------------------------
    // DELETE /api/tasklists/{taskListId}/tasks/{taskId}
    // → Task existiert NICHT → 404
    // ------------------------------------------------------------
    @Test
    void testDeleteTask_NotFound() {
        String listId = createTaskList();

        given()
                .when()
                .delete("/tasklists/" + listId + "/tasks/" + UUID.randomUUID())
                .then()
                .log().body()
                .statusCode(404)
                .body("message", equalTo("Ressource nicht gefunden"));
    }
}
