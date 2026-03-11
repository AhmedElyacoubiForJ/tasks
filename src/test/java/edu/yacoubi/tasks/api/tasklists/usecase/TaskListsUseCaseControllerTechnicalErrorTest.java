package edu.yacoubi.tasks.api.tasklists.usecase;

import edu.yacoubi.tasks.api.base.TaskApiRestAssuredTestBase;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * TaskListsUseCaseControllerTechnicalErrorTest
 * --------------------------------------------
 * Testet alle technischen Fehlerfälle der TaskList-Use-Case-API,
 * die NICHT durch Domainregeln verursacht werden.
 *
 * Abgedeckte Fehlertypen:
 *   - Ungültiges JSON (400)
 *   - Falscher Content-Type (415)
 *   - Fehlende oder ungültige Request-Bodies (400)
 *   - Fehlerhafte Payload-Struktur (400)
 *   - Nicht parsebare Werte (400)
 *
 * Abgedeckte Endpunkte (Use-Cases):
 *   - PUT    /api/tasklists/{id}/archive
 *   - POST   /api/tasklists/{id}/activate
 *   - PATCH  /api/tasklists/{id}/status
 *   - POST   /api/tasklists/{id}/tasks
 *   - PATCH  /api/tasklists/{id}/tasks/{taskId}/status
 *   - DELETE /api/tasklists/{id}/tasks/{taskId}
 *
 * Zweck:
 *   Sicherstellen, dass die API bei technischen Fehlern korrekt reagiert,
 *   unabhängig von fachlichen Domainregeln.
 *
 * NICHT enthalten:
 *   - CRUD-Fehler (404 / 405)
 *   - Domainregel-Verletzungen (409 / 422)
 *   - Happy-Path-Use-Cases (200 / 204)
 *
 * Aufruf (CLI):
 *   ./mvnw test -Dtest=TaskListsUseCaseControllerTechnicalErrorTest
 */
class TaskListsUseCaseControllerTechnicalErrorTest extends TaskApiRestAssuredTestBase {

    // ------------------------------------------------------------
    // ARCHIVE: Ungültiges JSON → 400
    // ------------------------------------------------------------
    //@Test
    void testArchive_InvalidJson() {
        String id = createTaskList("Liste");

        given()
                .contentType(ContentType.JSON)
                .body("{ invalid json }")
                .when()
                .put("/tasklists/" + id + "/archive")
                .then()
                .statusCode(400)
                .body("message", equalTo("Ungültiges JSON"));
    }

    // ------------------------------------------------------------
    // ACTIVATE: Falscher Content-Type → 415
    // ------------------------------------------------------------
    //@Test
    void testActivate_WrongContentType() {
        String id = createTaskList("Liste");

        given()
                .contentType(ContentType.XML)
                .when()
                .post("/tasklists/" + id + "/activate")
                .then()
                .statusCode(415)
                .body("message", equalTo("Unsupported Media Type"));
    }

    // ------------------------------------------------------------
    // STATUSWECHSEL: Body fehlt → 400
    // ------------------------------------------------------------
    //@Test
    void testChangeStatus_EmptyBody() {
        String id = createTaskList("Liste");

        given()
                .contentType(ContentType.JSON)
                .body("")
                .when()
                .patch("/tasklists/" + id + "/status")
                .then()
                .statusCode(400)
                .body("message", equalTo("Ungültige Anfrage"));
    }

    // ------------------------------------------------------------
    // STATUSWECHSEL: Nicht parsebarer Enum-Wert → 400
    // ------------------------------------------------------------
    //@Test
    void testChangeStatus_InvalidEnum() {
        String id = createTaskList("Liste");

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "status": "NOT_A_REAL_STATUS"
                        }
                        """)
                .when()
                .patch("/tasklists/" + id + "/status")
                .then()
                .statusCode(400)
                .body("message", equalTo("Ungültige Anfrage"));
    }

    // ------------------------------------------------------------
    // TASK ERSTELLEN: Ungültiges JSON → 400
    // ------------------------------------------------------------
    @Test
    void testCreateTask_InvalidJson() {
        String id = createTaskList("Liste");

        given()
                .contentType(ContentType.JSON)
                .body("{ invalid json }")
                .when()
                .post("/tasklists/" + id + "/tasks")
                .then()
                .statusCode(400)
                .body("message", equalTo("Ungültiges JSON"));
    }

    // ------------------------------------------------------------
    // TASK STATUSWECHSEL: Body fehlt → 400
    // ------------------------------------------------------------
    //@Test
    void testChangeTaskStatus_EmptyBody() {
        String id = createTaskList("Liste");
        String taskId = createTask(id);

        given()
                .contentType(ContentType.JSON)
                .body("")
                .when()
                .patch("/tasklists/" + id + "/tasks/" + taskId + "/status")
                .then()
                .statusCode(400)
                .body("message", equalTo("Ungültige Anfrage"));
    }

    // ------------------------------------------------------------
    // TASK STATUSWECHSEL: Falscher Content-Type → 415
    // ------------------------------------------------------------
    //@Test
    void testChangeTaskStatus_WrongContentType() {
        String id = createTaskList("Liste");
        String taskId = createTask(id);

        given()
                .contentType(ContentType.XML)
                .when()
                .patch("/tasklists/" + id + "/tasks/" + taskId + "/status")
                .then()
                .statusCode(415)
                .body("message", equalTo("Unsupported Media Type"));
    }
}
