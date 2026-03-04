package edu.yacoubi.tasks.api.tasklists.tasks.crud;

import edu.yacoubi.tasks.api.base.TaskApiRestAssuredTestBase;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * TaskListsTasksControllerValidationTest
 * --------------------------------------
 * Platzhalter für alle Validierungs-Tests der Endpunkte unter:
 *
 *   /api/tasklists/{taskListId}/tasks
 *
 * Aufruf (CLI):
 *   ./mvnw test -Dtest=TaskListsTasksControllerValidationTest
 *
 * Zweck:
 *   Diese Klasse enthält Tests, die sicherstellen, dass ungültige Eingaben
 *   korrekt mit 400 BAD REQUEST beantwortet werden.
 *
 * Typische Validierungsfälle:
 *   - Leerer oder fehlender "title"
 *   - Ungültige "priority" Werte
 *   - Ungültiges oder fehlendes JSON
 *   - Ungültige Datumsformate (z. B. dueDate)
 *   - Zu lange Strings (title, description)
 *   - PATCH/PUT mit komplett leerem Body
 *
 * Abgedeckte Endpunkte:
 *   - POST   /api/tasklists/{taskListId}/tasks
 *   - PATCH  /api/tasklists/{taskListId}/tasks/{taskId}
 *   - PUT    /api/tasklists/{taskListId}/tasks/{taskId}
 */
class TaskListsTasksCrudControllerValidationTest extends TaskApiRestAssuredTestBase {

    // ------------------------------------------------------------
    // POST /api/tasklists/{taskListId}/tasks
    // → title fehlt → 400 Validierungsfehler
    // ------------------------------------------------------------
    @Test
    void testCreateTask_MissingTitle() {
        String listId = createTaskList();

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "description": "Desc",
                          "priority": "HIGH"
                        }
                        """)
                .when()
                .post("/tasklists/" + listId + "/tasks")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Validierungsfehler"))
                .body("errors[0].message", equalTo("title: Titel darf nicht leer sein"));
    }

    // ------------------------------------------------------------
    // POST /api/tasklists/{taskListId}/tasks
    // → priority ungültig → 400 Validierungsfehler
    // ------------------------------------------------------------
    @Test
    void testCreateTask_InvalidPriority() {
        String listId = createTaskList();

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "title": "Test",
                          "description": "Desc",
                          "priority": "INVALID"
                        }
                        """)
                .when()
                .post("/tasklists/" + listId + "/tasks")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Validierungsfehler"))
                .body("errors[0].message", equalTo("priority: Ungültiger Wert"));
    }

    // ------------------------------------------------------------
    // PUT /api/tasklists/{taskListId}/tasks/{taskId}
    // → Body leer → 400 Validierungsfehler
    // ------------------------------------------------------------
    @Test
    void testFullUpdateTask_EmptyBody() {
        String listId = createTaskList();
        createTask(listId);

        String taskId =
                given().when().get("/tasklists/" + listId + "/tasks").then().extract().path("data[0].id");

        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .put("/tasklists/" + listId + "/tasks/" + taskId)
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Validierungsfehler"));
    }

    // ------------------------------------------------------------
    // PATCH /api/tasklists/{taskListId}/tasks/{taskId}
    // → Ungültiges JSON → 400 Ungültiges JSON
    // ------------------------------------------------------------
    @Test
    void testPatchTask_InvalidJson() {
        String listId = createTaskList();
        createTask(listId);

        String taskId =
                given().when().get("/tasklists/" + listId + "/tasks").then().extract().path("data[0].id");

        given()
                .contentType(ContentType.JSON)
                .body("{ invalid json }")
                .when()
                .patch("/tasklists/" + listId + "/tasks/" + taskId)
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Ungültiges JSON"))
                .body("errors[0].message", equalTo("JSON konnte nicht gelesen werden"));
    }
}

